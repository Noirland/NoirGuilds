package me.zephirenz.noirguilds.listeners;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public PlayerJoinListener() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GuildMember gMember = gHandler.getGuildMember(player.getName());
        if(gMember == null) {
            return;
        }
        Guild guild = gMember.getGuild();
        String[] motd = guild.getMotd();

        for(String line : motd) {
            line = ChatColor.translateAlternateColorCodes("&".charAt(0), line);
            plugin.sendMessage(player, line);
        }
    }

}