package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Commandlet {

    protected final NoirGuilds plugin = NoirGuilds.inst();
    protected final GuildsHandler gHandler = plugin.getGuildsHandler();

    public abstract void run(CommandSender sender, String[] args);

    protected boolean checkPlayer(CommandSender sender, String error) {
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, error);
            return false;
        }
        return true;
    }
}
