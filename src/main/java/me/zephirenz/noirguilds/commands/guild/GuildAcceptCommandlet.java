package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.config.PluginConfig;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildAcceptCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final PluginConfig pConfig;

    public GuildAcceptCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.pConfig = PluginConfig.getInstance();
    }

    /**
     *  The commandlet for accepting invites.
     *  Usage: /guild accept
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

        if(data.getGuild().getMembers().size() >= pConfig.getMemberLimit() && pConfig.getMemberLimit() > 0) {
            plugin.sendMessage(sender, GUILD_AT_MAX);
            return;
        }

        Guild guild = data.getGuild();
        GuildRank rank = guild.getDefaultRank();
        GuildMember gMember = new GuildMember(data.getInvitee(), guild, rank);
        guild.addGuildMember(gMember);
        gHandler.addMember(gMember);

        gHandler.sendMessageToGuild(guild, String.format(Strings.GUILD_ACCEPT_JOINED, rank.getColour() + data.getInvitee()));

    }

}
