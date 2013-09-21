package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GuildsHandler {

    private NoirGuilds plugin;
    private DatabaseManager dbManager;
    ArrayList<Guild> guilds = new ArrayList<Guild>();

    public GuildsHandler() {
        this.plugin = NoirGuilds.inst();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
        updateGuildsList();
    }

    public void updateGuildsList() {

         for(Guild guildA : dbManager.getGuilds()) {
             boolean inArray = false;
             for(Guild guildB : guilds) {
                 if(guildA.equals(guildB)) {
                     inArray = true;
                 }
             }
             if(!inArray) {
                 guilds.add(guildA);
             }
         }
    }

    public ArrayList<Guild> getGuilds() {
        return guilds;
    }

    public void addGuild(Guild guild) {
        guilds.add(guild);
        dbManager.createGuild(guild);
    }

    public void addRank(GuildRank rank) {

    }

    public void addMember(GuildMember member) {

    }

    public GuildMember getGuildPlayer(String player) {
        for(Guild guild : guilds) {
            for(GuildMember member : guild.getMembers()) {
                if(member.getPlayer().equals(player)) {
                    return member;
                }
            }
        }
        return null;
    }

    public void sendMessageToGuild(Guild guild, String msg) {
        for(GuildMember gPlayer : guild.getMembers()) {
            Player player = plugin.getServer().getPlayer(gPlayer.getPlayer());
            if(player != null) {
                player.sendMessage(msg);
            }
        }
    }

    public void sendMessageToRank(Guild guild, GuildRank rank, String msg) {
        for(GuildMember gPlayer : guild.getMembers()) {
            if(!(gPlayer.getRank().equals(rank))) {
                return;
            }
            Player player = plugin.getServer().getPlayer(gPlayer.getPlayer());
            if(player != null) {
                player.sendMessage(msg);
            }
        }
    }

    public boolean hasPerm(GuildMember member, String perm) {
        return hasPerm(member.getRank(), perm);
    }

    public boolean hasPerm(GuildRank rank, String perm) {
        if(rank.isLeader()) {
            return true;
        }else{
            return rank.getPerm(perm);
        }
    }
}
