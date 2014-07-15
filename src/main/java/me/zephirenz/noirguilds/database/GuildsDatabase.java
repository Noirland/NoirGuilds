package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.PluginConfig;
import me.zephirenz.noirguilds.database.schema.Schema1;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.MySQLDatabase;

import java.util.List;

public class GuildsDatabase extends MySQLDatabase {

    private static GuildsDatabase inst;

    PluginConfig config = PluginConfig.getInstance();

    public static GuildsDatabase inst() {
        if(inst == null) {
            return new GuildsDatabase();
        }
        return inst;
    }

    private GuildsDatabase() {
        schemas.put(1, new Schema1());
    }

    @Override
    protected Debug debug() {
        return NoirGuilds.debug();
    }

    @Override
    protected String getHost() {
        return config.getDBHost();
    }

    @Override
    protected int getPort() {
        return config.getDBPort();
    }

    @Override
    protected String getDatabase() {
        return config.getDBName();
    }

    @Override
    protected String getUsername() {
        return config.getDBUser();
    }

    @Override
    protected String getPassword() {
        return config.getDBPassword();
    }

    @Override
    protected String getPrefix() {
        return config.getDBPrefix();
    }

    public List<Guild> getGuilds() {
        /*
        Get all Guild object.
        Get all GuildRank objects, linking to respective guilds.
        Get all GuildMembers, linking to ranks and guilds.
         */
        return null;
    }

    public void updateGuild(Guild guild) {

    }

    public void removeGuild(Guild guild) {

    }

    public void updateMember(GuildMember member) {

    }

    public void removeMember(GuildMember member) {

    }

    public void updateRank(GuildRank rank) {

    }

    public void removeRank(GuildRank rank) {

    }


}
