package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.objects.Guild;

import java.util.ArrayList;

public interface DatabaseManager {

    public ArrayList<Guild> getGuilds();

    public void createGuild(Guild guild);

    public void saveAll();

    public void close();


}
