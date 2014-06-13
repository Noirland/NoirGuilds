package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.NO_CONSOLE;
import static me.zephirenz.noirguilds.Strings.NO_INVITE;

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
            plugin.sendMessage(sender, NO_CONSOLE);
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
            plugin.sendMessage(sender, NO_INVITE);
            return;
        }

        inviteTask.cancel();
        gHandler.removeInvite(inviteTask);

        OfflinePlayer pSender = Util.player(data.getSender());
        if(pSender.isOnline()) {
            plugin.sendMessage(pSender.getPlayer(), String.format(Strings.GUILD_DENY_DENIED, data.getInvitee()));
        }
        plugin.sendMessage(sender, String.format(Strings.GUILD_DENY_DENIED, data.getGuild().getName()));

    }

}
