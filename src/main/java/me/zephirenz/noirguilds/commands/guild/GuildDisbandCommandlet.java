package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.Perms;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildDisbandCommandlet extends Commandlet {

    /**
     * The commandlet for disbanding a guild.
     * Usage: /guild disband (guild)
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        String name;
        Guild guild;
        if(args.length == 1 && sender.hasPermission(Perms.DISBAND_OTHER)) {
            name = args[0];
            guild = gHandler.getGuildByName(name);
        }else{
            if(!checkPlayer(sender, GUILD_DISBAND_CONSOLE_GUILD)) return;

            GuildMember gMember = gHandler.getMember((Player) sender);
            if (gMember == null) {
                plugin.sendMessage(sender, GUILD_DISBAND_NO_GUILD);
                return;
            }
            guild = gMember.getGuild();
            if(!gMember.getRank().isLeader()) {
                plugin.sendMessage(sender, GUILD_DISBAND_NOT_LEADER);
                return;
            }
        }

        gHandler.removeGuild(guild);

        GuildsDatabase db = GuildsDatabase.inst();
        db.removeGuild(guild);
        for(GuildRank rank : guild.getRanks()) {
            db.removeRank(rank);
        }
        for(GuildMember member : guild.getMembers()) {
            db.removeMember(member);
        }
        plugin.sendGlobalMessage(String.format(GUILD_DISBAND_DISBANDED, guild.getName()));

    }

}
