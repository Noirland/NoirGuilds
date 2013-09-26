package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildsCommand implements CommandExecutor {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildsCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        StringBuilder sb = new StringBuilder("Guilds: ");

        String delim = "";
        for(Guild guild : gHandler.getGuilds()) {
            sb.append(delim).append(ChatColor.BLUE + guild.getName() + ChatColor.GRAY + " [" + guild.getTag() + "]" + ChatColor.RESET);
            delim = ", ";
        }
        plugin.sendMessage(sender, sb.toString());
        return true;
    }

}
