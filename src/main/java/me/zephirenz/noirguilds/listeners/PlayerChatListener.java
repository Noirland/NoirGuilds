package me.zephirenz.noirguilds.listeners;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private NoirGuilds plugin;
    private GuildsHandler gHandler;

    public PlayerChatListener() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {

        String guildString = "";
        String player = event.getPlayer().getName();
        GuildMember gMember = gHandler.getGuildMember(player);
        if(gMember != null) {
            Guild guild = gMember.getGuild();
            guildString = " [" + guild.getTag() + "]";

        }

        String format = event.getFormat();
        format = format.replace("{GUILD}", guildString);
        event.setFormat(format);
    }

}
