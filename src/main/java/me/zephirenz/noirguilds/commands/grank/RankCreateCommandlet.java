package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class RankCreateCommandlet extends Commandlet {

    /**
     *  The commandlet for creating a rank.
     *  Usage: /grank create [rank]
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(!checkPlayer(sender, NO_CONSOLE)) return;

        GuildMember gMember = gHandler.getMember((Player) sender);

        if(gMember == null) {
            plugin.sendMessage(sender, RANK_CREATE_NO_GUILD);
            return;
        }
        if(!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, RANK_CREATE_NOT_LEADER);
            return;
        }

        if(args.length != 1){
            plugin.sendMessage(sender, RANK_CREATE__WRONG_ARGS);
            return;
        }
        String name = args[0];

        for(GuildRank rank : gMember.getGuild().getRanks()) {
            if(rank.getName().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, RANK_EXISTS);
                return;
            }
        }

        GuildRank newRank = new GuildRank(gHandler.createRankID(), gMember.getGuild(), name, null, ChatColor.WHITE);
        GuildsDatabase.inst().addRank(newRank);
        plugin.sendMessage(sender, String.format(RANK_CREATE_CREATED, newRank.getColour() + newRank.getName()));
    }
}
