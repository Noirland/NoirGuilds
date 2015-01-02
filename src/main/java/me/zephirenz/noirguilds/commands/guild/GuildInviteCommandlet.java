package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.GuildsConfig;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildInviteCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final GuildsConfig pConfig;

    public GuildInviteCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.pConfig = GuildsConfig.inst();
    }

    /**
     *  The commandlet for inviting players.
     *  Usage: /guild invite [player]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }
        if(args.length != 1) {
            plugin.sendMessage(sender, GUILD_INVITE_WRONG_ARGS);
            return;
        }
        OfflinePlayer inviter = (Player) sender;
        OfflinePlayer invitee = Util.player(args[0]);
        GuildMember inviterMember = gHandler.getMember(inviter);
        GuildMember inviteeMember = gHandler.getMember(invitee);
        if(!invitee.isOnline()) {
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
        if(!inviterMember.hasPerm(RankPerm.INVITE)) {
            plugin.sendMessage(sender, GUILD_INVITE_NO_PERMS);
            return;
        }
        if(inviterMember.getGuild().getMembers().size() >= pConfig.getMemberLimit() && pConfig.getMemberLimit() > 0) {
            plugin.sendMessage(sender, GUILD_AT_MAX);
            return;
        }
        InviteData inviteData = new InviteData(inviter.getUniqueId(), invitee.getUniqueId(), inviterMember.getGuild());
        new GuildInviteTask(inviteData);
    }

}
