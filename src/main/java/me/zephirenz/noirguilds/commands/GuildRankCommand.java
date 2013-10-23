package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.commands.grank.*;
import me.zephirenz.noirguilds.enums.GuildRankCommandlet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class GuildRankCommand implements CommandExecutor {

    private final NoirGuilds plugin;

    public GuildRankCommand() {
        this.plugin = NoirGuilds.inst();
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) {
//            helpCommandlet();
            return true;
        }
        GuildRankCommandlet cmd;
        try{
            cmd = GuildRankCommandlet.valueOf(args[0].toLowerCase());
        }catch(IllegalArgumentException e) {
            plugin.sendMessage(sender, "Command not found.");
            return true;
        }


        String[] cmdletArgs = Arrays.copyOfRange(args, 1, args.length);
        switch(cmd) {
            case create:
                new RankCreateCommandlet().run(sender, cmdletArgs);
                break;
            case delete:
                new RankDeleteCommandlet().run(sender, cmdletArgs);
                break;
            case edit:
                new RankEditCommandlet().run(sender, cmdletArgs);
                break;
            case set:
                new RankSetCommandlet().run(sender, cmdletArgs);
                break;
            case list:
                new RankListCommandlet().run(sender, cmdletArgs);
                break;
            default:
                break;
        }
        return true;
    }

}
