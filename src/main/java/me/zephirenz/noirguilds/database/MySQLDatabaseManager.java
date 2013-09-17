package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildPlayer;
import me.zephirenz.noirguilds.objects.GuildRank;

public class MySQLDatabaseManager implements DatabaseManager {
    public Guild getGuildByName(String guildName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Guild getGuildByOwner(String player) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Guild getGuildByPlayer(String player) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GuildPlayer getGPlayerByGuild(String guild) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GuildRank getPlayerRank(String player) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Guild[] getGuilds() {
        return new Guild[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
