package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.GuildConfig;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.Location;
import org.bukkit.World;

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
        config.setName(guild.getName());
        config.setTag(guild.getTag());
        config.setLeader(guild.getLeader());
        for(GuildRank rank : guild.getRanks()) {
            config.setRankColour(rank.getName(), rank.getColour());
            for(Map.Entry<RankPerm, Boolean> rankPerm : rank.getPerms().entrySet()) {
                config.setRankPerm(rank.getName(), rankPerm.getKey(), rankPerm.getValue());
            }
            if(rank.isLeader()) config.setRankLeader(rank.getName(), true);
            if(rank.isDefault()) config.setRankDefault(rank.getName(), true);

        }
        for(GuildMember member : guild.getMembers()) {
            config.addMember(member.getPlayer(), member.getRank().getName());
        }

    }

    public void removeGuild(Guild guild) {
        GuildConfig.getInstance(guild).deleteFile();
        GuildConfig.removeInstance(GuildConfig.getInstance(guild));
    }

    public void addMember(GuildMember member) {
        Guild guild = member.getGuild();
        GuildConfig config = GuildConfig.getInstance(guild);
        config.addMember(member.getPlayer(), member.getRank().getName());

    }

    public void removeMember(GuildMember member) {
        Guild guild = member.getGuild();
        GuildConfig config = GuildConfig.getInstance(guild);
        config.removeMember(member.getPlayer());
    }

    public void addRank(GuildRank rank) {
        Guild guild = rank.getGuild();
        GuildConfig config = GuildConfig.getInstance(guild);
//        config.addRank(rank.getName(), rank.getColour(), rank.getPerms());
        config.setRankColour(rank.getName(), rank.getColour());
        for(Map.Entry<RankPerm, Boolean> rankPerm : rank.getPerms().entrySet()) {
            config.setRankPerm(rank.getName(), rankPerm.getKey(), rankPerm.getValue());
        }
        if(rank.isLeader()) config.setRankLeader(rank.getName(), true);
        if(rank.isDefault()) config.setRankDefault(rank.getName(), true);
    }

    public void removeRank(GuildRank rank) {
        Guild guild = rank.getGuild();
        GuildConfig config = GuildConfig.getInstance(guild);
        config.removeRank(rank.getName());
    }

    public void updateGuildTag(Guild guild, String tag) {
        GuildConfig config = GuildConfig.getInstance(guild);
        config.setTag(tag);
//        config.renameFile(tag);

    }

    public void updateGuildName(Guild guild, String name) {
        GuildConfig config = GuildConfig.getInstance(guild);
        config.setName(name);
    }

    public void updateMemberRank(GuildMember member, GuildRank rank) {
        GuildConfig config = GuildConfig.getInstance(member.getGuild());
        config.setMemberRank(member.getPlayer(), rank.getName());
    }

    public Location getHq(Guild guild) {
        GuildConfig config = GuildConfig.getInstance(guild);

        World world = NoirGuilds.inst().getServer().getWorld(config.getHqWorld());
        double x = (double) config.getHqX();
        double y = (double) config.getHqY();
        double z = (double) config.getHqZ();
        float yaw = (float) config.getHqYaw();
        float pitch = (float) config.getHqPitch();

        return new Location(world, x, y, z, yaw, pitch);
    }

    public void setHq(Guild guild, Location loc) {
        GuildConfig config = GuildConfig.getInstance(guild);
        config.setHq(loc);
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
