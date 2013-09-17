package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildPlayer;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildChatCommand implements CommandExecutor {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildChatCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Consoles cannot send messages in guild chat.");
            return false;
        }
        Player player = (Player) sender;
        GuildPlayer gPlayer = plugin.toGuildPlayer(player.getName());
        if(gPlayer == null || gPlayer.getGuild() == null || gPlayer.getRank() == null) {
            //TODO: Error
            return false;
        }

        GuildRank rank = gPlayer.getRank();

        StringBuilder buffer = new StringBuilder();
        for (String arg : args) {
            buffer.append(' ').append(arg);
        }
        String prefix = ChatColor.RED + "[G]" + ChatColor.GRAY + " [" + rank.getColour() + rank.getName() + ChatColor.GRAY + "] "
                            + ChatColor.RESET + player.getName() + ChatColor.RED + ": " + ChatColor.RESET;
        String msg = prefix + buffer.toString();

        gHandler.sendMessageToGuild(gPlayer.getGuild(), msg);


        return false;
    }

}
