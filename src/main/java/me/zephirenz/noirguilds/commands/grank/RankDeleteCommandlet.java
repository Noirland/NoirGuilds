package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

            plugin.sendMessage(sender, "Console cannot delete guild ranks.");
            return;
        }
        GuildMember gMember = gHandler.getGuildMember(sender.getName());

        if(gMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to delete ranks.");
            return;
        }
        if(!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, "Only guild leaders can delete ranks.");
            return;
        }

        if(args.length != 1){
            plugin.sendMessage(sender, "You must only specify a rank name.");
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
            plugin.sendMessage(sender, "Rank not found.");
            return;
        }

        if(rank.isDefault()) {
            plugin.sendMessage(sender, "You can't delete the default rank.");
            return;
        }
        if(rank.isLeader()) {
            plugin.sendMessage(sender, "You can't delete the leader rank.");
            return;
        }

        guild.removeRank(rank);
        gHandler.removeRank(rank);
        plugin.sendMessage(sender, rank.getColour() + rank.getName() + ChatColor.RESET + " has been deleted.");

    }

}
