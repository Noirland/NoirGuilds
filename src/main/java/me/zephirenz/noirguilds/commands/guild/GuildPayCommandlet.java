package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildBankManager;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.zephcore.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static me.zephirenz.noirguilds.Strings.*;
import static nz.co.noirland.bankofnoir.Strings.AMOUNT_NAN;

public class GuildPayCommandlet extends Commandlet {

    private final GuildBankManager bManager = plugin.getBankManager();
    private final EcoManager eco = EcoManager.inst();

    /**
     *  The commandlet for paying another guild.
     *  Usage: /guild pay [guild] [amount]
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(!checkPlayer(sender, NO_CONSOLE)) return;

        GuildMember member = gHandler.getMember((Player) sender);
        if(isNull(member, sender, GUILD_PAY_NO_GUILD)) return;
        Guild fromGuild = member.getGuild();

        if(args.length != 2) {
            plugin.sendMessage(sender, GUILD_PAY_WRONG_ARGS);
            return;
        }

        if(!member.hasPerm(RankPerm.PAY)) {
            plugin.sendMessage(sender, GUILD_PAY_NO_PERMS);
            return;
        }

        Guild toGuild = gHandler.getGuildByName(args[0]);
        if(isNull(toGuild, sender, GUILD_NOT_EXISTS)) return;

        if(toGuild.equals(fromGuild)) {
            plugin.sendMessage(sender, GUILD_PAY_SELF);
            return;
        }

        String a = args[1].replace("$", "");
        double amount;
        try {
            amount = Double.parseDouble(a);
        } catch(NumberFormatException e) {
            plugin.sendMessage(sender, AMOUNT_NAN);
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

        toGuild.updateDB();
        fromGuild.updateDB();

        toGuild.getLeaderRank().sendMessage(String.format(GUILD_PAY_RECIEVED, eco.format(amount), fromGuild.getName()), true);

        plugin.sendMessage(sender, String.format(GUILD_PAY_SUCCESSFUL, eco.format(amount), toGuild.getName()));
    }

}
