package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.InviteData;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildInviteCommandlet {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildInviteCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
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
            plugin.sendMessage(sender, "You must specify a player to invite.");
            return;
        }
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not invite players.");
            return;
        }
        String inviter = sender.getName();
        String invitee = args[0];
        GuildMember inviterMember = gHandler.getGuildPlayer(inviter);
        GuildMember inviteeMember = gHandler.getGuildPlayer(invitee);
        if(plugin.getServer().getPlayer(invitee) == null) {
            plugin.sendMessage(sender, "You can only invite online players.");
            return;
        }
        if(inviterMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to invite players.");
            return;
        }
        if(inviteeMember != null) {
            plugin.sendMessage(sender, "That player is already in a guild.");
            return;
        }
        if(!gHandler.hasPerm(inviterMember, RankPerm.INVITE)) {
            plugin.sendMessage(sender, "You do not have permission to invite players.");
            return;
        }
        InviteData inviteData = new InviteData(inviter, invitee, inviterMember.getGuild());
        new GuildInviteTask(inviteData).runTaskLater(plugin, 60 * 20);




    }

}
