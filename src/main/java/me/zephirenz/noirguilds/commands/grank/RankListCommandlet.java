package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.NO_CONSOLE;
import static me.zephirenz.noirguilds.Strings.RANK_LIST_NO_GUILD;

public class RankListCommandlet extends Commandlet {

    /**
     *  The commandlet for listing all ranks.
     *  Usage: /grank list
     *   @param sender the sender of the command
     *
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(isNotPlayer(sender, NO_CONSOLE)) return;

        GuildMember gMember = gHandler.getMember((Player) sender);

        if(isNull(gMember, sender, RANK_LIST_NO_GUILD)) return;

        Guild guild = gMember.getGuild();

        StringBuilder sb = new StringBuilder("Ranks: ");

        String delim = "";
        for(GuildRank rank : guild.getRanks()) {
            sb.append(delim).append(rank.getColour()).append(rank.getName());
            delim = ChatColor.RESET + ", ";
        }
        plugin.sendMessage(sender, sb.toString());


    }
}
