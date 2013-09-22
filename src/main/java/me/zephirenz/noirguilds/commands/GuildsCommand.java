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

public class GuildsCommand implements CommandExecutor {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildsCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        String list = "Guilds: ";

        for(Guild guild : gHandler.getGuilds()) {
            list += ChatColor.BLUE + guild.getName() + ChatColor.GRAY + " [" + guild.getTag() + "]" + ChatColor.RESET + ", ";
        }
        plugin.sendMessage(sender, list);
        return true;
    }

}
