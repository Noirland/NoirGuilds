package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.commands.guild.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                new CreateCommandlet().run(sender, cmdletArgs);
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
}
