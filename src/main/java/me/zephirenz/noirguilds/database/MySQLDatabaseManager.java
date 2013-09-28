package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;

public class MySQLDatabaseManager implements DatabaseManager {

    public ArrayList<Guild> getGuilds() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createGuild(Guild guild) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeGuild(Guild guild) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addMember(GuildMember member) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeMember(GuildMember member) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addRank(GuildRank rank) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeRank(GuildRank rank) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateGuildTag(Guild guild, String tag) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateGuildName(Guild guild, String name) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateMemberRank(GuildMember mPromote, GuildRank newRank) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Location getHq(Guild guild) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setHq(Guild guild, Location loc) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateGuildName(Guild guild) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void saveAll() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateRankColour(GuildRank rank, ChatColor colour) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateRankName(GuildRank rank, String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateRankPerm(GuildRank rank, RankPerm perm, boolean val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // -- PRIVATE DATABASE FUNCTIONS --
}
