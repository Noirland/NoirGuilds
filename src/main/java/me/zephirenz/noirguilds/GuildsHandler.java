package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildPlayer;
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
                 if(guildA.getGuildName().equalsIgnoreCase(guildB.getGuildName())) {
                     inArray = true;
                 }
             }
             if(!inArray) {
                 guilds.add(guildA);
             }
         }
    }

    public Guild getGuildByPlayer(String player) {
        return dbManager.getGuildByPlayer(player);
    }

    public void sendMessageToGuild(Guild guild, String msg) {
        for(GuildPlayer gPlayer : guild.getGuildMemebers()) {
            Player player = plugin.getServer().getPlayer(gPlayer.getPlayer());
            if(player != null) {
                player.sendMessage(msg);
            }
        }
    }

    public void sendMessageToRank(Guild guild, GuildRank rank, String msg) {
        for(GuildPlayer gPlayer : guild.getGuildMemebers()) {
            if(!(gPlayer.getRank().equals(rank))) {
                return;
            }
            Player player = plugin.getServer().getPlayer(gPlayer.getPlayer());
            if(player != null) {
                player.sendMessage(msg);
            }
        }
    }
}
