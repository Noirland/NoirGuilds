package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class RankCreateCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public RankCreateCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for creating a rank.
     *  Usage: /grank create [rank]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {

            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }
        GuildMember gMember = gHandler.getGuildMember(sender.getName());

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
        if(name.contains(".")) {
            plugin.sendMessage(sender, RANK_NO_PERIODS);
            return;
        }
        for(GuildRank rank : gMember.getGuild().getRanks()) {
            if(rank.getName().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, RANK_EXISTS);
                return;
            }
        }

        GuildRank newRank = new GuildRank(gMember.getGuild(), name, null, ChatColor.WHITE);
        gMember.getGuild().addRank(newRank);
        gHandler.addRank(newRank);
        plugin.sendMessage(sender, String.format(RANK_CREATE_CREATED, newRank.getColour() + newRank.getName()));

    }
}
