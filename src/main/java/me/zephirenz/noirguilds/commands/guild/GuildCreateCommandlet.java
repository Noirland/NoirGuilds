package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildCreateCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildCreateCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for creating guilds.
     *  Usage: /guild create [guild] [tag] (leader)
     *
     *  @param sender the sender of the command
     *  @param args   commandlet specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(args.length < 2) {
            plugin.sendMessage(sender, "Not enough arguments!");
//            helpCommandlet(sender, args, GuildCommandlet.create);
            return;
        }
        String name = args[0];
        String tag = args[1];
        String leader;


        if(args.length == 3 && sender.hasPermission("noirguilds.create.other")) {
            leader = args[2];
        }else{
            if(sender instanceof Player) {
                leader = sender.getName();
            }else{
                plugin.sendMessage(sender, "Console must specify a leader");
                return;
            }
        }

        if(!sender.hasPermission("noirguilds.create")) {
            plugin.sendMessage(sender, "You don't have permission to create guilds.");
            return;
        }

        if(gHandler.getGuildMember(leader) != null) {
            plugin.sendMessage(sender, "Already in a guild!");
            return;
        }
        if(!GuildsUtil.isValidTag(tag)) {
            if(tag.length() <= 4) {
                plugin.sendMessage(sender, "Tags must be a maximum of 4 characters.");
            }
            plugin.sendMessage(sender, "Tags must only contain letters, numbers, periods, dashes, and underscores.");
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getName().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, "A guild with that name already exists.");
                return;
            }
            if(g.getTag().equalsIgnoreCase(tag)) {
                plugin.sendMessage(sender, "A guild with that tag already exists.");
                return;
            }
        }

        Guild guild = new Guild(name, tag, leader);

        GuildRank leaderRank = new GuildRank(guild, "Leader", null, ChatColor.DARK_RED);
        leaderRank.setLeader(true);
        guild.addRank(leaderRank);

        GuildRank defRank = new GuildRank(guild, "Default", null, ChatColor.BLUE);
        defRank.setDefault(true);
        guild.addRank(defRank);

        GuildMember leaderMember = new GuildMember(leader, guild, leaderRank);
        guild.addGuildMember(leaderMember);

        gHandler.addGuild(guild);
        plugin.sendGlobalMessage(guild.getLeader() + " has just founded " + name);
    }

}
