package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;

import java.util.ArrayList;

public interface DatabaseManager {

    public ArrayList<Guild> getGuilds();

    public void createGuild(Guild guild);

    public void removeGuild(Guild guild);

    public void addMember(GuildMember member);

    public void removeMember(GuildMember member);

    public void saveAll();


    public void close();
}
