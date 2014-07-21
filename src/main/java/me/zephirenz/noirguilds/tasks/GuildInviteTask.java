package me.zephirenz.noirguilds.tasks;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.InviteData;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildInviteTask extends BukkitRunnable {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final InviteData data;

    public GuildInviteTask(InviteData data) {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.data = data;

        Player sender = Util.player(data.getSender()).getPlayer();
        Player invitee = Util.player(data.getInvitee()).getPlayer();
        plugin.sendMessage(invitee, String.format(GUILD_INVITE_INVITED, data.getGuild().getName(), Util.player(data.getSender()).getName()));
        plugin.sendMessage(invitee, GUILD_INVITE_CMD_HELP);
        plugin.sendMessage(sender, GUILD_INVITE_SENT);

        gHandler.addInvite(this);
        runTaskLater(plugin, 60 * 20);
    }

    public InviteData getData() {
        return data;
    }

    public void run() {
        OfflinePlayer sender = Util.player(data.getSender());
        OfflinePlayer invitee = Util.player(data.getInvitee());
        if(!invitee.isOnline()) {
            this.cancel();
        }
        if(gHandler.getInvites().contains(this)) {
            gHandler.removeInvite(this);
            if(sender.isOnline()) {
                plugin.sendMessage(sender.getPlayer(), String.format(GUILD_INVITE_EXPIRED, Util.player(data.getInvitee()).getName()));
            }
            plugin.sendMessage(invitee.getPlayer(), String.format(GUILD_INVITE_EXPIRED, data.getGuild().getName()));
        }
    }


}
