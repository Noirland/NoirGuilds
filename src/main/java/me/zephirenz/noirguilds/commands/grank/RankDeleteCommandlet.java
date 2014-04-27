package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class RankDeleteCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public RankDeleteCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for deleting a rank.
     *  Usage: /grank delete [rank]
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
            plugin.sendMessage(sender, RANK_DELETE_NO_GUILD);
            return;
        }
        if(!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, RANK_DELETE_NOT_LEADER);
            return;
        }

        if(args.length != 1){
            plugin.sendMessage(sender, RANK_DELETE_WRONG_ARGS);
            return;
        }
        String name = args[0];

        Guild guild = gMember.getGuild();
        GuildRank rank = null;

        for(GuildRank r : guild.getRanks()) {
            if(r.getName().equalsIgnoreCase(name)) {
                rank = r;
            }
        }
        if(rank == null) {
            plugin.sendMessage(sender, RANK_NOT_EXISTS);
            return;
        }

        if(rank.isDefault()) {
            plugin.sendMessage(sender, RANK_DELETE_DEFAULT);
            return;
        }
        if(rank.isLeader()) {
            plugin.sendMessage(sender, RANK_DELETE_LEADER);
            return;
        }

        guild.removeRank(rank);
        gHandler.removeRank(rank);
        plugin.sendMessage(sender, String.format(RANK_DELETE_DELETED, rank.getColour() + rank.getName()));

    }

}
