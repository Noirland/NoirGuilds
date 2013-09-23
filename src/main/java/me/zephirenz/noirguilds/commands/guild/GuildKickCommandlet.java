package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildKickCommandlet {

    NoirGuilds plugin;
    GuildsHandler gHandler;

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
            plugin.sendMessage(sender, "You must specify a player to kick.");
            return;
        }
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not kick players from guilds.");
            return;
        }
        String kickee = args[0];
        GuildMember senderMember = gHandler.getGuildMember(sender.getName());
        GuildMember kickeeMember = gHandler.getGuildMember(kickee);

        if(senderMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to leave players.");
            return;
        }
        Guild guild = senderMember.getGuild();

        boolean inGuild = false;
        if(kickeeMember != null && senderMember.getGuild().getMembers().contains(kickeeMember)) {
                inGuild = true;
        }
        if(!inGuild) {
            plugin.sendMessage(sender, "Player must be in your guild to kick.");
            return;
        }
        if(kickeeMember.getRank().isLeader()) {
            plugin.sendMessage(sender, "You can't kick the guild leader.");
            return;
        }
        if(!gHandler.hasPerm(senderMember, RankPerm.KICK)) {
            plugin.sendMessage(sender, "You do not have permission to kick players.");
            return;
        }

        guild.removeGuildMember(kickeeMember);
        gHandler.removeGuildMember(kickeeMember);
        gHandler.sendMessageToGuild(guild, kickee + " was kicked from the guild.");


    }

}
