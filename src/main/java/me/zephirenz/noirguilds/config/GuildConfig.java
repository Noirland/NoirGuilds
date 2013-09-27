package me.zephirenz.noirguilds.config;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuildConfig extends Config {

    private Guild guild;

    private static Map<File, GuildConfig> instances = new HashMap<File, GuildConfig>();

    public static GuildConfig getInstance(File file) {
        if(!instances.containsKey(file)) {
            instances.put(file, new GuildConfig(file));
        }
        return instances.get(file);
    }

    public static Map<File, GuildConfig> getInstances() {
        return instances;
    }

    public static GuildConfig getInstance(Guild guild) {
        if(!instances.containsKey(guildToFile(guild))) {
            instances.put(guildToFile(guild), new GuildConfig(guildToFile(guild)));
        }
        return instances.get(guildToFile(guild));
    }

    public static void removeInstance(GuildConfig config) {
        if(instances.containsValue(config)) {
            instances.remove(config.configFile);
        }
    }

    public GuildConfig(File file) {
        super(file);
    }

    @Override
    protected InputStream getResource() {
        return plugin.getResource("guild.yml");
    }

    public String getName()   { return config.getString("name");   }
    public String getTag()    { return config.getString("tag");    }
    public String getLeader() { return config.getString("leader"); }

    public boolean isRankLeader(String rank)  { return config.getBoolean("ranks." + rank + ".leader", false); }
    public boolean isRankDefault(String rank) { return config.getBoolean("ranks." + rank + ".default", false); }

    public ArrayList<GuildMember> getMembers() {

        Map<String, Object> members = config.getConfigurationSection("members").getValues(false);
        ArrayList<GuildMember> ret = new ArrayList<GuildMember>();

        for(Map.Entry<String, Object> entry : members.entrySet()) {
            if(entry.getValue() instanceof String) {
                String rank = (String) entry.getValue() ;
                ret.add(new GuildMember(entry.getKey(), guild, getRank(rank)));
            }
        }
        return ret;

    }

    public ArrayList<GuildRank> getRanks() {

        ConfigurationSection ranks = config.getConfigurationSection("ranks");
        ArrayList<GuildRank> ret = new ArrayList<GuildRank>();

        for(String rank : ranks.getKeys(false)) {
            ConfigurationSection rankInfo = ranks.getConfigurationSection(rank);
            Map<RankPerm, Boolean> permsList = getPerms(rankInfo);
            GuildRank grank = new GuildRank(guild, rankInfo.getName(), permsList, ChatColor.valueOf(rankInfo.getString("colour")));
            grank.setDefault(rankInfo.getBoolean("default", false));
            grank.setLeader(rankInfo.getBoolean("leader", false));
            ret.add(grank);
        }
        return ret;

    }

    public GuildRank getRank(String rank) {
        ConfigurationSection rankInfo = config.getConfigurationSection("ranks." + rank);
        for(GuildRank gRank : getRanks()) {
            if(gRank.getName().equals(rank)) {
                return gRank;
            }
        }
        return null;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
        saveFile();
    }

    public void setName(String name) {
        config.set("name", name);
        saveFile();
    }
    public void setTag(String tag) {
        config.set("tag", tag);
        saveFile();
    }

    public void setLeader(String leader) {
        config.set("leader", leader);
        saveFile();
    }

    public void setRankColour(String rank, ChatColor colour) {
        config.set("ranks." + rank + ".colour", colour.name());
        saveFile();
    }

    public void setRankPerm(String rank, RankPerm perm, boolean val) {
        config.set("ranks." + rank + "." + perm.getPerm(), val);
        saveFile();
    }

    public void setRankDefault(String rank, boolean val) {
        config.set("ranks." + rank + ".default", val);
        saveFile();
    }

    public void setRankLeader(String rank, boolean val) {
        config.set("ranks." + rank + ".leader", val);
        saveFile();
    }

    public void setMemberRank(String member, String rank) {
        config.set("members." + member, rank);
        saveFile();
    }

    public void addMember(String player, String rank) {
        config.set("members." + player, rank);
        saveFile();
    }

    public void removeRank(String rank) {
        config.getConfigurationSection("ranks").set(rank, null);
        saveFile();
    }

    public void removeMember(String member) {
        config.getConfigurationSection("members").set(member, null);
        saveFile();
    }

    private static File guildToFile(Guild guild) {
        return new File(new File(NoirGuilds.inst().getDataFolder(), "guilds"), guild.getTag() + ".yml");
    }

    private Map<RankPerm, Boolean> getPerms(ConfigurationSection rank) {
        Map<RankPerm, Boolean> ret = new HashMap<RankPerm, Boolean>();
        for(RankPerm perm : RankPerm.values()) {
            String pName = perm.getPerm();
            if(rank.contains(pName)) {
                ret.put(perm, rank.getBoolean(pName));
            }
        }
        return ret;
    }



}
