package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCreateCommandlet {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public RankCreateCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for creating a rank.
     *  Usage: /guild create [rank]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {

            plugin.sendMessage(sender, "Console cannot create guild ranks.");
            return;
        }
        GuildMember gMember = gHandler.getGuildMember(sender.getName());

        if(gMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to create ranks.");
            return;
        }
        if(!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, "Only guild leaders can create ranks.");
            return;
        }

        if(args.length != 1){
            plugin.sendMessage(sender, "You must only specify a rank name.");
            return;
        }
        String name = args[0];

        GuildRank newRank = new GuildRank(gMember.getGuild(), name, null, ChatColor.WHITE);
        gMember.getGuild().addRank(newRank);
        gHandler.addRank(newRank);

    }
}
