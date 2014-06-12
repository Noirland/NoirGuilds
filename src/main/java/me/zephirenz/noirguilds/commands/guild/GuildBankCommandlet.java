package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.BankManager;
import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Perms;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildBankCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final BankManager bManager;

    public GuildBankCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.bManager = plugin.getBankManager();
    }

    /**
     *  The commandlet for viewing the guild's bank.
     *  Usage: /guild bank (guild)
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }

        if(!bManager.isEnabled()) {
            plugin.sendMessage(sender, BANKS_NOT_ENABLED);
        }

        Guild guild;
        if(args.length > 0 && sender.hasPermission(Perms.BANK_OTHER)) {
            guild = gHandler.getGuild(args[0]);
            if(guild == null) {
                plugin.sendMessage(sender, GUILD_NOT_EXISTS);
                return;
            }
        } else {
            String player = sender.getName();
            GuildMember member = gHandler.getGuildMember(player);
            if(member == null) {
                plugin.sendMessage(sender, GUILD_BANK_NO_GUILD);
                return;
            }
            if(!gHandler.hasPerm(member, RankPerm.BANK_VIEW)) {
                plugin.sendMessage(sender, GUILD_BANK_NO_PERMS);
                return;
            }
            guild = member.getGuild();
        }
        Player p = (Player) sender;
        p.openInventory(bManager.getBank(guild).getBank());
    }

}
