package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Perms;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

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
            plugin.sendMessage(sender, GUILD_CREATE_WRONG_ARGS);
//            helpCommandlet(sender, args, GuildCommandlet.create);
            return;
        }
        String name = args[0];
        String tag = args[1];
        String leader;


        if(args.length == 3 && sender.hasPermission(Perms.CREATE_OTHER)) {
            leader = args[2];
        }else{
            if(sender instanceof Player) {
                leader = sender.getName();
            }else{
                plugin.sendMessage(sender, GUILD_CREATE_CONSOLE_LEADER);
                return;
            }
        }

        if(!sender.hasPermission(Perms.CREATE)) {
            plugin.sendMessage(sender, GUILD_CREATE_NO_PERMS);
            return;
        }

        if(gHandler.getGuildMember(leader) != null) {
            plugin.sendMessage(sender, GUILD_CREATE_IN_GUILD);
            return;
        }
        if(!GuildsUtil.isValidTag(tag)) {
            if(tag.length() <= 4) {
                plugin.sendMessage(sender, BIG_TAG);
            }
            plugin.sendMessage(sender, BAD_TAG_CHARS);
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getName().equalsIgnoreCase(name) || g.getTag().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, GUILD_EXISTS);
                return;
            }
            if(g.getTag().equalsIgnoreCase(tag) || g.getName().equals(tag)) {
                plugin.sendMessage(sender, TAG_EXISTS);
                return;
            }
        }

        Guild guild = new Guild(name, tag, leader);

        GuildRank leaderRank = new GuildRank(guild, DEFAULT_LEADER, null, ChatColor.DARK_RED);
        leaderRank.setLeader(true);
        guild.addRank(leaderRank);

        GuildRank defRank = new GuildRank(guild, DEFAULT_DEFAULT, null, ChatColor.BLUE);
        defRank.setDefault(true);
        guild.addRank(defRank);

        GuildMember leaderMember = new GuildMember(leader, guild, leaderRank, kills, deaths);
        guild.addMember(leaderMember);

        gHandler.addGuild(guild);
        plugin.sendGlobalMessage(String.format(GUILD_CREATE_CREATED, guild.getLeader(), name));
    }

}
