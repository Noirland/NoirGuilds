package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.PluginConfig;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildInviteCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final PluginConfig pConfig;

    public GuildInviteCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.pConfig = PluginConfig.getInstance();
    }

    /**
     *  The commandlet for inviting players.
     *  Usage: /guild invite [player]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(args.length != 1) {
            plugin.sendMessage(sender, GUILD_INVITE_WRONG_ARGS);
            return;
        }
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }
        String inviter = sender.getName();
        String invitee = args[0];
        GuildMember inviterMember = gHandler.getGuildMember(inviter);
        GuildMember inviteeMember = gHandler.getGuildMember(invitee);
        if(!Util.player(invitee).isOnline()) {
            plugin.sendMessage(sender, PLAYER_NOT_ONLINE);
            return;
        }
        if(inviterMember == null) {
            plugin.sendMessage(sender, GUILD_INVITE_NO_GUILD);
            return;
        }
        if(inviteeMember != null) {
            plugin.sendMessage(sender, GUILD_INVITE_TARGET_IN_GUILD);
            return;
        }
        if(!gHandler.hasPerm(inviterMember, RankPerm.INVITE)) {
            plugin.sendMessage(sender, GUILD_INVITE_NO_PERMS);
            return;
        }
        if(inviterMember.getGuild().getMembers().size() >= pConfig.getMemberLimit() && pConfig.getMemberLimit() > 0) {
            plugin.sendMessage(sender, GUILD_AT_MAX);
            return;
        }
        InviteData inviteData = new InviteData(inviter, invitee, inviterMember.getGuild());
        new GuildInviteTask(inviteData).runTaskLater(plugin, 60 * 20);
    }

}
