package me.zephirenz.noirguilds.tasks;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.InviteData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GuildInviteTask extends BukkitRunnable {

    private NoirGuilds plugin;
    private GuildsHandler gHandler;
    private InviteData data;

    public GuildInviteTask(InviteData data) {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.data = data;

        Player sender = plugin.getServer().getPlayer(data.getSender());
        Player invitee = plugin.getServer().getPlayer(data.getInvitee());
        plugin.sendMessage(invitee, "You have been invited to join " + ChatColor.BLUE + data.getGuild().getName() + ChatColor.RESET + " by " + data.getSender());
        plugin.sendMessage(invitee, "To accept the invitation, do " + ChatColor.DARK_GRAY + "/guild accept");
        plugin.sendMessage(sender, "Your invitation has been sent.");

        gHandler.addInvite(this);

    }

    public InviteData getData() {
        return data;
    }

    public void run() {

        Player sender = plugin.getServer().getPlayer(data.getSender());
        Player invitee = plugin.getServer().getPlayer(data.getInvitee());
        if(invitee == null) {
            this.cancel();
        }
        if(gHandler.getInvites().contains(this)) {
            gHandler.removeInvite(this);
            if(sender != null) {
                plugin.sendMessage(sender, "Your invite to " + data.getInvitee() + " has expired.");
            }
            plugin.sendMessage(invitee, "You invite to " + data.getGuild().getName() + " has expired.");
        }
    }


}
