package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.commands.grank.*;
import me.zephirenz.noirguilds.enums.GuildRankCommandlet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GuildRankCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final Map<GuildRankCommandlet, Commandlet> commandlets = new HashMap<GuildRankCommandlet, Commandlet>();

    {
        commandlets.put(GuildRankCommandlet.CREATE, new RankCreateCommandlet());
        commandlets.put(GuildRankCommandlet.DELETE, new RankDeleteCommandlet());
        commandlets.put(GuildRankCommandlet.EDIT, new RankEditCommandlet());
        commandlets.put(GuildRankCommandlet.SET, new RankSetCommandlet());
        commandlets.put(GuildRankCommandlet.LIST, new RankListCommandlet());
    }

    public GuildRankCommand() {
        this.plugin = NoirGuilds.inst();
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) {
            //TODO: Help
            return true;
        }
        GuildRankCommandlet cmd;
        try{
            cmd = GuildRankCommandlet.valueOf(args[0].toUpperCase());
        }catch(IllegalArgumentException e) {
            plugin.sendMessage(sender, Strings.NO_COMMAND);
            return true;
        }

        String[] cmdletArgs = Arrays.copyOfRange(args, 1, args.length);
        commandlets.get(cmd).run(sender, cmdletArgs);
        return true;
    }

}
