package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Commandlet {

    protected final NoirGuilds plugin = NoirGuilds.inst();
    protected final GuildsHandler gHandler = plugin.getGuildsHandler();

    public abstract void run(CommandSender sender, String[] args);

    protected boolean isNotPlayer(CommandSender sender, String error) {
        if(sender instanceof Player) return false;
        plugin.sendMessage(sender, error);
        return true;
    }

    protected boolean isNull(Object object, CommandSender sender, String error) {
        if(object != null) return false;
        plugin.sendMessage(sender, error);
        return true;
    }

    protected boolean isNotNull(Object object, CommandSender sender, String error) {
        if(object == null) return false;
        plugin.sendMessage(sender, error);
        return true;
    }

    protected boolean isNotLeader(GuildMember member, CommandSender sender, String error) {
        if(member.getRank().isLeader()) return false;
        plugin.sendMessage(sender, error);
        return true;
    }
}
