package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.commands.guild.*;
import me.zephirenz.noirguilds.enums.GuildCommandlet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class GuildCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final Map<GuildCommandlet, Commandlet> commands = new HashMap<GuildCommandlet, Commandlet>();

    {
        commands.put(GuildCommandlet.INFO, new GuildInfoCommandlet());
        commands.put(GuildCommandlet.CREATE, new GuildCreateCommandlet());
        commands.put(GuildCommandlet.INVITE, new GuildInviteCommandlet());
        commands.put(GuildCommandlet.KICK, new GuildKickCommandlet());
//        commands.put(GuildCommandlet.EDIT, new GuildEditCommandlet());
        commands.put(GuildCommandlet.LEAVE, new GuildLeaveCommandlet());
        commands.put(GuildCommandlet.DISBAND, new GuildDisbandCommandlet());
        commands.put(GuildCommandlet.ACCEPT, new GuildAcceptCommandlet());
        commands.put(GuildCommandlet.DENY, new GuildDenyCommandlet());
        commands.put(GuildCommandlet.MOTD, new GuildMOTDCommandlet());
        commands.put(GuildCommandlet.BANK, new GuildBankCommandlet());
        commands.put(GuildCommandlet.PAY, new GuildPayCommandlet());
    }

    public GuildCommand() {
        this.plugin = NoirGuilds.inst();
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 0) {
            //TODO: Display help
            return true;
        }

        GuildCommandlet cmd;
        try{
            cmd = GuildCommandlet.valueOf(args[0].toUpperCase());
        }catch(IllegalArgumentException e) {
            plugin.sendMessage(sender, Strings.NO_COMMAND);
            return true;
        }

        String[] cmdletArgs = Arrays.copyOfRange(args, 1, args.length);

        commands.get(cmd).run(sender, cmdletArgs);
        return true;
    }
}
