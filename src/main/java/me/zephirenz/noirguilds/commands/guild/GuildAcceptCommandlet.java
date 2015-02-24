package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildAcceptCommandlet extends Commandlet {

    /**
     *  The commandlet for accepting invites.
     *
     *  Usage: /guild accept
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

        if(data.getGuild().isFull()) {
            plugin.sendMessage(sender, String.format(GUILD_AT_MAX, data.getGuild().getMemberLimit()));
            return;
        }

        Guild guild = data.getGuild();
        GuildRank rank = guild.getDefaultRank();
        GuildMember member = new GuildMember(data.getInvitee(), rank, 0, 0);
        GuildsDatabase.inst().addMember(member);

        guild.sendMessage(String.format(Strings.GUILD_ACCEPT_JOINED, rank.getColour() + sender.getName()), true);
    }

}
