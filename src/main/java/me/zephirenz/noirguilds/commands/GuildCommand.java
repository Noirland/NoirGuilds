package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GuildCommand implements CommandExecutor {

    /*
    - /guilds info [guild]/[tag]
    - /guilds list

    - /guild create [guild] [tag] (leader)
    - /guild invite [player]
    - /guild kick [player]
    - /guild edit [option] [value]
        - name | string
        - tag | string (<= 4 chars)
        - friendlyfire | boolean
    - /guild disband (guild)
    */

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    private enum GuildCommandlets {
        info,
        list,
        create,
        invite,
        kick,
        edit,
        disband
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) {
//            helpCommandlet();
        }
        String[] cmdletArgs = Arrays.copyOfRange(args, 1, args.length);
        switch(GuildCommandlets.valueOf(args[0].toLowerCase())) {
//            case info:
//                infoCommandlet(sender, cmdletArgs);
//                break;
//            case list:
//                listCommandlet();
//                break;
            case create:
                createCommandlet(sender, cmdletArgs);
                break;
//            case invite:
//                inviteCommandlet(sender, cmdletArgs);
//                break;
//            case kick:
//                kickCommandlet(sender, cmdletArgs);
//                break;
//            case edit:
//                editCommandlet(sender, cmdletArgs);
//                break;
//            case disband:
//                disbandCommandlet(sender, cmdletArgs);
//                break;
//            default:
//                helpCommandlet(sender, cmdletArgs, null);
//                break;
        }
        return true;
    }
    /**
     *  The commandlet for creating guilds.
     *  Usage: /guild create [guild] [tag] (leader)
     *
     *  @param sender the sender of the command
     *  @param args   commandlet specific args
     */
    private void createCommandlet(CommandSender sender, String[] args) {
        if(args.length < 2) {
            plugin.sendMessage(sender, "Not enough arguments!");
//            helpCommandlet(sender, args, GuildCommandlets.create);
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
        if(gHandler.getGuildPlayer(leader) != null) {
            plugin.sendMessage(sender, "Already in a guild!");
            return;
        }
        if(!isValidTag(tag)) {
            if(tag.length() <= 4) {
                plugin.sendMessage(sender, "Tags must be a maximum of 4 characters.");
            }
            plugin.sendMessage(sender, "Tags must only contain letters, numbers, periods, dashes, and underscores.");
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getGuildName().equals(name)) {
                plugin.sendMessage(sender, "A guild with that name already exists.");
                return;
            }
            if(g.getTag().equals(tag)) {
                plugin.sendMessage(sender, "A guild with that tag already exists.");
                return;
            }
        }

        Guild guild = new Guild(name, tag, leader);

        GuildRank leaderRank = new GuildRank(guild, "Leader", null, ChatColor.DARK_RED);
        leaderRank.setPerm("leader", true);
        guild.addRank(leaderRank);

        GuildRank defRank = new GuildRank(guild, "Default", null, ChatColor.BLUE);
        defRank.setPerm("default", true);
        guild.addRank(defRank);

        GuildMember leaderMember = new GuildMember(leader, guild, leaderRank);
        guild.addGuildMember(leaderMember);

        gHandler.addGuild(guild);
        plugin.sendGlobalMessage(guild.getLeader() + " has just founded " + name);
    }

    /**
     * Check whether a tag is valid (ie on alphanumeric, period, dash or underscore)
     * @param tag the tag to be checked
     */
    private boolean isValidTag(String tag) {
        return tag.matches("[a-zA-Z0-9.-_]+") && tag.length() <= 4;
    }
}
