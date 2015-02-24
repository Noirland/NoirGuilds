package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildBankManager;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.config.GuildsConfig;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.EcoManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildUpgradeCommandlet extends Commandlet {

    private final Map<CommandSender, UpgradeConfirmTask> confirming = new HashMap<CommandSender, UpgradeConfirmTask>();

    private final GuildBankManager bank = plugin.getBankManager();

    @Override
    public void run(CommandSender sender, String[] args) {
        if(isNotPlayer(sender, NO_CONSOLE)) return;

        GuildMember member = gHandler.getMember((Player) sender);
        if(isNull(member, sender, GUILD_UPGRADE_NO_GUILD)) return;
        Guild guild = member.getGuild();
        if(isNotLeader(member, sender, GUILD_UPGRADE_NOT_LEADER)) return;

        if(confirming.containsKey(sender)) {
            confirming.remove(sender);

            double balance = guild.getBalance();
            double cost = getUpgradeCost(guild);
            if(balance < cost) {
                plugin.sendMessage(sender, GUILD_UPGRADE_INSUFFICIENT_MONEY);
                return;
            }
            double newBalance = balance - cost;

            guild.setBalance(newBalance);
            bank.updateBank(guild, -cost);

            guild.setMemberLimit(guild.getMemberLimit() + 1);

            guild.updateDB();

            plugin.sendMessage(sender, String.format(GUILD_UPGRADE_COMPLETE, guild.getMemberLimit()));
            return;
        }

        EcoManager eco = EcoManager.inst();
        plugin.sendMessage(sender, String.format(GUILD_UPGRADE_INFO, guild.getMemberLimit() + 1, eco.format(getUpgradeCost(guild))));

        confirming.put(sender, new UpgradeConfirmTask(sender));

        /*
        PROCESS:
        - Owner runs /guild upgrade
        - Gets cost to upgrade members
          - Get default members in config
          - For each member up to members+1, add 1.5x initial cost.
        - Inform owner of cost and upgrade, that it will be paid from guild account, and to confirm again.
        - Start timeout task
        - When player accepts, check amount in balance, and subtract
        - Add to player limit
        - update db
         */
    }

    private int getUpgradeCost(Guild guild) {
        GuildsConfig config = GuildsConfig.inst();

        int members = guild.getMemberLimit();
        int initMembers = config.getInitialMemberLimit();
        int diffMembers = members - initMembers;
        int cost = config.getInitialUpgradeCost();
        double mult = config.getUpgradeMultiplier();
        return (int) (cost * Math.pow(mult, diffMembers));
    }

    class UpgradeConfirmTask extends BukkitRunnable {
        private CommandSender sender;

        public UpgradeConfirmTask(CommandSender sender) {
            this.sender = sender;
            this.runTaskLater(NoirGuilds.inst(), 20 * 60); // 1 minute delay
        }

        @Override
        public void run() {
            confirming.remove(sender);
        }
    }
}
