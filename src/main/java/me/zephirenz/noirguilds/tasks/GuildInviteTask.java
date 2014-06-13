package me.zephirenz.noirguilds.tasks;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.InviteData;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        plugin.sendMessage(invitee, "You have been invited to join " + ChatColor.BLUE + data.getGuild().getName() + ChatColor.RESET + " by " + data.getSender());
        plugin.sendMessage(invitee, "To accept the invitation, do " + ChatColor.DARK_GRAY + "/guild accept");
        plugin.sendMessage(sender, "Your invitation has been sent.");

        gHandler.addInvite(this);

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
                plugin.sendMessage(sender.getPlayer(), "Your invite to " + data.getInvitee() + " has expired.");
            }
            plugin.sendMessage(invitee.getPlayer(), "You invite to " + data.getGuild().getName() + " has expired.");
        }
    }


}
