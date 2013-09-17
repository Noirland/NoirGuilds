package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildPlayer;
import me.zephirenz.noirguilds.objects.GuildRank;

public interface DatabaseManager {

    public Guild getGuildByName(String guildName);

    public Guild getGuildByOwner(String player);

    public Guild getGuildByPlayer(String player);

    public GuildPlayer getGPlayerByGuild(String guild);

    public GuildRank getPlayerRank(String player);

    public Guild[] getGuilds();


}
