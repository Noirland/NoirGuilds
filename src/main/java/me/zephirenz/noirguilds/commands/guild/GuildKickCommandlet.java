package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildKickCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildKickCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for kicking a player from a guild.
     *  Usage: /guild kick [player]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(args.length != 1) {
            plugin.sendMessage(sender, GUILD_KICK_WRONG_ARGS);
            return;
        }
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }
        String kickee = args[0];
        GuildMember senderMember = gHandler.getGuildMember(sender.getName());
        GuildMember kickeeMember = gHandler.getGuildMember(kickee);

        if(senderMember == null) {
            plugin.sendMessage(sender, GUILD_KICK_NO_GUILD);
            return;
        }
        Guild guild = senderMember.getGuild();

        boolean inGuild = false;
        if(kickeeMember != null && senderMember.getGuild().getMembers().contains(kickeeMember)) {
                inGuild = true;
        }
        if(!inGuild) {
            plugin.sendMessage(sender, GUILD_KICK_BAD_TAGET);
            return;
        }
        if(kickeeMember.getRank().isLeader()) {
            plugin.sendMessage(sender, GUILD_KICK_LEADER);
            return;
        }
        if(!senderMember.hasPerm(RankPerm.KICK)) {
            plugin.sendMessage(sender, GUILD_KICK_NO_PERMS);
            return;
        }

        guild.removeMember(kickeeMember);
        gHandler.removeGuildMember(kickeeMember);
        guild.sendMessage(String.format(GUILD_KICK_KICKED, kickee));
    }

}
