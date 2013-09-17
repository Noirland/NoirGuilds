package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildPlayer;

public interface DatabaseManager {

    public Guild getGuildByName(String guildName);

    public Guild getGuildByOwner(String player);

    public Guild getGuildByPlayer(String player);

    public GuildPlayer getPlayerByGuild(String player);

    public Guild[] getGuilds();


}
