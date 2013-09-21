package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildAcceptCommandlet {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildAcceptCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    /**
     *  The commandlet for accepting invites.
     *  Usage: /guild accept [player]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not accept invites.");
            return;
        }
        InviteData data = null;
        GuildInviteTask inviteTask = null;
        for(GuildInviteTask task : gHandler.getInvites()) {
            InviteData id = task.getData();
            if(id.getInvitee().equals(sender.getName())) {
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

        Guild guild = data.getGuild();
        GuildRank rank = guild.getDefaultRank();
        GuildMember gMember = new GuildMember(data.getInvitee(), guild, rank);
        guild.addGuildMember(gMember);
        gHandler.addMember(gMember);

        gHandler.sendMessageToGuild(guild, rank.getColour() + data.getInvitee() + ChatColor.RESET + " has joined the guild!");

    }

}
