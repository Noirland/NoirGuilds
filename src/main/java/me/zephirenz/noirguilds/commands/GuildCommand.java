package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.commands.guild.*;
import me.zephirenz.noirguilds.enums.GuildCommandlet;
import me.zephirenz.noirguilds.enums.GuildRankCommandlet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class GuildCommand implements CommandExecutor {

    /*
    - /guilds info [guild]/[tag]
    - /guilds list

    X /guild create [guild] [tag] (leader)
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

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) {
//            helpCommandlet();
            return true;
        }

        GuildCommandlet cmd;
        try{
            cmd = GuildCommandlet.valueOf(args[0].toLowerCase());
        }catch(IllegalArgumentException e) {
            plugin.sendMessage(sender, "Command not found.");
            return true;
        }

        String[] cmdletArgs = Arrays.copyOfRange(args, 1, args.length);
        switch(cmd) {
            case info:
                new GuildInfoCommandlet().run(sender, cmdletArgs);
                break;
//            case list:
//                listCommandlet();
//                break;
            case create:
                new GuildCreateCommandlet().run(sender, cmdletArgs);
                break;
            case invite:
                new GuildInviteCommandlet().run(sender, cmdletArgs);
                break;
            case kick:
                new GuildKickCommandlet().run(sender, cmdletArgs);
                break;
            case edit:
                new GuildEditCommandlet().run(sender, cmdletArgs);
                break;
            case leave:
                new GuildLeaveCommandlet().run(sender, cmdletArgs);
                break;
            case disband:
                new GuildDisbandCommandlet().run(sender, cmdletArgs);
                break;
            case accept:
                new GuildAcceptCommandlet().run(sender, cmdletArgs);
                break;
            case deny:
                new GuildDenyCommandlet().run(sender, cmdletArgs);
                break;
            default:
                //helpCommandlet(sender, cmdletArgs, null);
                break;
        }
        return true;
    }
}
