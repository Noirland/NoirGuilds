package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.NO_CONSOLE;
import static me.zephirenz.noirguilds.Strings.NO_INVITE;

public class GuildDenyCommandlet extends Commandlet {

    /**
     *  The commandlet for denying invites.
     *  Usage: /guild deny
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(isNotPlayer(sender, NO_CONSOLE)) return;

        InviteData data = null;
        GuildInviteTask inviteTask = null;
        for(GuildInviteTask task : gHandler.getInvites()) {
            InviteData id = task.getData();
            if(id.getInvitee().equals(((Player) sender).getUniqueId())) {
                data = id;
                inviteTask = task;
            }
        }

        if(isNull(data, sender, NO_INVITE)) return;

        inviteTask.cancel();
        gHandler.removeInvite(inviteTask);

        OfflinePlayer pSender = Util.player(data.getSender());
        if(pSender.isOnline()) {
            plugin.sendMessage(pSender.getPlayer(), String.format(Strings.GUILD_DENY_DENIED, Util.player(data.getInvitee()).getName()));
        }
        plugin.sendMessage(sender, String.format(Strings.GUILD_DENY_DENIED, data.getGuild().getName()));

    }

}
