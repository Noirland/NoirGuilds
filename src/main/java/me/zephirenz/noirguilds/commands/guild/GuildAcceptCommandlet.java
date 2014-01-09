package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.PluginConfig;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if(data.getGuild().getMembers().size() >= pConfig.getMemberLimit() && pConfig.getMemberLimit() > 0) {
            plugin.sendMessage(sender, "The guild has the maximum number of members.");
            return;
        }

        Guild guild = data.getGuild();
        GuildRank rank = guild.getDefaultRank();
        GuildMember gMember = new GuildMember(data.getInvitee(), guild, rank);
        guild.addGuildMember(gMember);
        gHandler.addMember(gMember);

        gHandler.sendMessageToGuild(guild, rank.getColour() + data.getInvitee() + ChatColor.RESET + " has joined the guild!");

    }

}
