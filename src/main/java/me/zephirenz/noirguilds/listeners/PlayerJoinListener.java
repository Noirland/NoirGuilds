package me.zephirenz.noirguilds.listeners;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class PlayerJoinListener implements Listener {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public PlayerJoinListener() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GuildMember member = gHandler.getMember(player);
        if(member == null) {
            return;
        }

        List<String> motd = member.getGuild().getMotd();

        if(motd == null) return;

        for(String line : motd) {
            line = ChatColor.translateAlternateColorCodes("&".charAt(0), line);
            plugin.sendMessage(player, line);
        }
    }

}