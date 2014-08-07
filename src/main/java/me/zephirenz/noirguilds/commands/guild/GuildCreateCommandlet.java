package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Perms;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildCreateCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildCreateCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for creating guilds.
     *  Usage: /guild create [guild] [tag] (leader)
     *
     *  @param sender the sender of the command
     *  @param args   commandlet specific args
     */
    public void run(CommandSender sender, String[] args) {
        if(args.length < 2) {
            plugin.sendMessage(sender, GUILD_CREATE_WRONG_ARGS);
            return;
        }
        String name = args[0];
        String tag = args[1];

        UUID leader;
        if(args.length == 3 && sender.hasPermission(Perms.CREATE_OTHER)) {
            leader = Util.uuid(args[2]);
        }else{
            if (!(sender instanceof Player)) {
                plugin.sendMessage(sender, GUILD_CREATE_CONSOLE_LEADER);
                return;
            }
            leader = ((Player) sender).getUniqueId();
        }

        if(!sender.hasPermission(Perms.CREATE)) {
            plugin.sendMessage(sender, GUILD_CREATE_NO_PERMS);
            return;
        }

        if(gHandler.getMember(leader) != null) {
            plugin.sendMessage(sender, GUILD_CREATE_IN_GUILD);
            return;
        }
        if(tag.length() > 4) {
            plugin.sendMessage(sender, BIG_TAG);
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getName().equalsIgnoreCase(name) || g.getTag().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, GUILD_EXISTS);
                return;
            }
            if(g.getTag().equalsIgnoreCase(tag) || g.getName().equals(tag)) {
                plugin.sendMessage(sender, TAG_EXISTS);
                return;
            }
        }

        GuildsDatabase db = GuildsDatabase.inst();
        Guild guild = new Guild(gHandler.createGuildID(), name, tag, 0, 0, 0, null, null);
        db.addGuild(guild);

        GuildRank leaderRank = new GuildRank(gHandler.createRankID(), guild, DEFAULT_LEADER, null, ChatColor.DARK_RED);
        leaderRank.setLeader();
        db.addRank(leaderRank);

        GuildRank defRank = new GuildRank(gHandler.createRankID(), guild, DEFAULT_DEFAULT, null, ChatColor.BLUE);
        defRank.setDefault();
        db.addRank(defRank);

        db.addMember(new GuildMember(leader, leaderRank, 0, 0));

        gHandler.addGuild(guild);
        plugin.sendGlobalMessage(String.format(GUILD_CREATE_CREATED, Util.player(leader).getName(), name));
    }

}
