package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildLeaveCommandlet {

    NoirGuilds plugin;
    GuildsHandler gHandler;

    public GuildLeaveCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for leaving a guild.
     *  Usage: /guild leave
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not leave guilds.");
            return;
        }
        GuildMember member = gHandler.getGuildMember(sender.getName());

        if(member == null) {
            plugin.sendMessage(sender, "You must be in a guild to leave it.");
            return;
        }
        Guild guild = member.getGuild();
        if(member.getRank().isLeader()) {
            plugin.sendMessage(sender, "Leaders cannot leave their own guild.");
            return;
        }

        guild.removeGuildMember(member);
        gHandler.removeGuildMember(member);
        gHandler.sendMessageToGuild(guild, member.getPlayer() + " left the guild.");
        plugin.sendMessage(sender, "You have left " + guild.getName());

    }

}
