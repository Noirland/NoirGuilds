package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.GuildConfig;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class FlatfileDatabaseManager implements DatabaseManager {

    public ArrayList<Guild> getGuilds() {
        ArrayList<Guild> guilds = new ArrayList<Guild>();
        File guildDir = new File(NoirGuilds.inst().getDataFolder(), "guilds");
        guildDir.mkdir();
        File[] files = guildDir.listFiles();
        if (files != null) {
            for(File file : files) {
                GuildConfig config = GuildConfig.getInstance(file);
                Guild guild = new Guild(config.getName(), config.getTag(), config.getLeader());
                config.setGuild(guild);
                populateGuild(guild);
                guilds.add(guild);
            }
        }
        return guilds;
    }

    public void createGuild(Guild guild) {
        GuildConfig config = GuildConfig.getInstance(guild); // Creates new config file for guild
        config.setGuild(guild);
        config.setName(guild.getGuildName());
        config.setTag(guild.getTag());
        config.setLeader(guild.getLeader());
        for(GuildRank rank : guild.getRanks()) {
            config.setRankColour(rank.getName(), rank.getColour());
            for(Map.Entry<String, Boolean> rankPerm : rank.getPerms().entrySet()) {
                config.setRankPerm(rank.getName(), rankPerm.getKey(), rankPerm.getValue());
            }

        }
        for(GuildMember member : guild.getMembers()) {
            config.addMember(member.getPlayer(), member.getRank().getName());
        }

    }

    public void saveAll() {
        for(Entry conf : GuildConfig.getInstances().entrySet()) {
            GuildConfig config = (GuildConfig) conf.getValue();
            config.saveFile();
        }
    }

    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // -- PRIVATE DATABASE FUNCTIONS --
    private void populateGuild(Guild guild) {
        GuildConfig config = GuildConfig.getInstance(guild);
        guild.setRanks(config.getRanks());
        guild.setMembers(config.getMembers());
    }
}
