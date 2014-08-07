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

    private final GuildsHandler gHandler;

    public PlayerChatListener() {
        this.gHandler = NoirGuilds.inst().getGuildsHandler();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        String guildString = "";
        GuildMember member = gHandler.getMember(event.getPlayer());
        if(member != null) {
            Guild guild = member.getGuild();
            guildString = " [" + guild.getTag() + "]";
        }

        String format = event.getFormat();
        format = format.replace("{GUILD}", guildString);
        event.setFormat(format);
    }

}
