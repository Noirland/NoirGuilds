package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildBankManager;
import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.BankOfNoir;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.bankofnoir.Strings;
import nz.co.noirland.zephcore.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildPayCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final DatabaseManager dbManager;
    private final GuildBankManager bManager;
    private final EcoManager eco;

    public GuildPayCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.bManager = plugin.getBankManager();
        this.eco = EcoManager.inst();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
    }


    /**
     *  The commandlet for paying another guild.
     *  Usage: /guild pay [guild] [amount]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        GuildMember member = gHandler.getGuildMember(sender.getName());
        if(member == null) {
            plugin.sendMessage(sender, GUILD_PAY_NO_GUILD);
            return;
        }
        Guild fromGuild = member.getGuild();


        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }

        if(args.length != 2) {
            plugin.sendMessage(sender, GUILD_PAY_WRONG_ARGS);
            return;
        }

        if(!gHandler.hasPerm(member, RankPerm.PAY)) {
            plugin.sendMessage(sender, GUILD_PAY_NO_PERMS);
            return;
        }

        Guild toGuild = gHandler.getGuildByName(args[0]);
        if(toGuild == null) {
            plugin.sendMessage(sender, GUILD_NOT_EXISTS);
            return;
        }

        if(toGuild.equals(fromGuild)) {
            plugin.sendMessage(sender, GUILD_PAY_SELF);
            return;
        }

        String a = args[1].replace("$", "");
        double amount;
        try {
            amount = Double.parseDouble(a);
        } catch(NumberFormatException e) {
            plugin.sendMessage(sender, Strings.AMOUNT_NAN);
            return;
        }
        if(amount < 0) {
            plugin.sendMessage(sender, GUILD_PAY_NEGATIVE_AMOUNT);
            return;
        }
        if(amount == 0) {
            plugin.sendMessage(sender, GUILD_PAY_ZERO_AMOUNT);
            return;
        }
        amount = Util.round(amount, new DecimalFormat("#.##")); // Round to two decimal places

        Double fromBal = fromGuild.getBalance();
        Double toBal = toGuild.getBalance();

        if(fromBal < amount) {
            plugin.sendMessage(sender, String.format(GUILD_PAY_INSUFFICIENT_BALANCE, eco.format(amount - fromBal)));
            return;
        }

        bManager.updateBank(toGuild, amount);
        bManager.updateBank(fromGuild, -amount);

        toGuild.setBalance(toBal + amount);
        fromGuild.setBalance(fromBal - amount);

        dbManager.setBalance(toGuild, toGuild.getBalance());
        dbManager.setBalance(fromGuild, fromGuild.getBalance());

        if(Util.player(toGuild.getLeader()).isOnline()) {
            BankOfNoir.sendMessage(Util.player(toGuild.getLeader()).getPlayer(), String.format(GUILD_PAY_RECIEVED, eco.format(amount), fromGuild.getName()));
        }

        BankOfNoir.sendMessage(sender, String.format(GUILD_PAY_SUCCESSFUL, eco.format(amount), toGuild.getName()));
    }

}
