package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Util;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GuildChatCommand implements CommandExecutor {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildChatCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.debug("Command /g | " + Arrays.toString(args));
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Consoles cannot send messages in guild chat.");
            return true;
        }
        Player player = (Player) sender;
        GuildMember gPlayer = gHandler.getGuildMember(player.getName());
        if(gPlayer == null || gPlayer.getGuild() == null || gPlayer.getRank() == null) {
            plugin.sendMessage(sender, "You are not currently in a guild.");
            return false;
        }

        GuildRank rank = gPlayer.getRank();
        String prefix = ChatColor.RED + "[G]" + ChatColor.GRAY + " [" + rank.getColour() + rank.getName() + ChatColor.GRAY + "] "
                            + ChatColor.RESET + player.getName() + ChatColor.RED + ":" + ChatColor.RESET;
        String msg = prefix + Util.arrayToString(args, 0, args.length - 1, " ");
        if(!(msg.length() == prefix.length())) {
            gHandler.sendMessageToGuild(gPlayer.getGuild(), msg);
            return true;
        }

        return false;
    }

}
