package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildKickCommandlet extends Commandlet {

    /**
     *  The commandlet for kicking a player from a guild.
     *  Usage: /guild kick [player]
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(isNotPlayer(sender, NO_CONSOLE)) return;

        if(args.length != 1) {
            plugin.sendMessage(sender, GUILD_KICK_WRONG_ARGS);
            return;
        }
        String kickee = args[0];
        GuildMember senderMember = gHandler.getMember((Player) sender);
        GuildMember kickeeMember = gHandler.getMember(kickee);

        if(isNull(senderMember, sender, GUILD_KICK_NO_GUILD)) return;
        Guild guild = senderMember.getGuild();

        if(!senderMember.hasPerm(RankPerm.KICK)) {
            plugin.sendMessage(sender, GUILD_KICK_NO_PERMS);
            return;
        }

        if(!senderMember.getGuild().getMembers().contains(kickeeMember)) {
            plugin.sendMessage(sender, GUILD_KICK_BAD_TAGET);
            return;
        }
        if(kickeeMember.getRank().isLeader()) {
            plugin.sendMessage(sender, GUILD_KICK_LEADER);
            return;
        }

        guild.removeMember(kickeeMember);
        GuildsDatabase.inst().removeMember(kickeeMember);
        guild.sendMessage(String.format(GUILD_KICK_KICKED, kickee), true);
    }

}
