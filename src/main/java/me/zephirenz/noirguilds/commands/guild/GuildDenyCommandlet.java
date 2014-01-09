package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildDenyCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildDenyCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    /**
     *  The commandlet for denying invites.
     *  Usage: /guild deny
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not deny invites.");
            return;
        }

        InviteData data = null;
        GuildInviteTask inviteTask = null;
        for(GuildInviteTask task : gHandler.getInvites()) {
            InviteData id = task.getData();
            if(id.getInvitee().equalsIgnoreCase(sender.getName())) {
                data = id;
                inviteTask = task;
            }
        }

        if(data == null) {
            plugin.sendMessage(sender, "You have no pending guild invite.");
            return;
        }

        inviteTask.cancel();
        gHandler.removeInvite(inviteTask);

        Player pSender = plugin.getServer().getPlayer(data.getSender());
        if(pSender != null) {
            plugin.sendMessage(pSender, "Your invite to " + data.getInvitee() + "  has been denied.");
        }
        plugin.sendMessage(sender, "Your invite to " + data.getGuild().getName() + " has been denied.");

    }

}
