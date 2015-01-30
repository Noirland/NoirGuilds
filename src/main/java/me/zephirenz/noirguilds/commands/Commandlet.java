package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import org.bukkit.command.CommandSender;

public abstract class Commandlet {

    protected final NoirGuilds plugin = NoirGuilds.inst();
    protected final GuildsHandler gHandler = plugin.getGuildsHandler();

    public abstract void run(CommandSender sender, String[] args);

}
