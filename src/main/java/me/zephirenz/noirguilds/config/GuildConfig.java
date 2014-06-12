package me.zephirenz.noirguilds.config;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.InputStream;
import java.util.*;

public class GuildConfig extends Config {

    private Guild guild;

    private static final Map<File, GuildConfig> instances = new HashMap<File, GuildConfig>();

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

    public String getName()    { return config.getString("name");       }
    public String getTag()     { return config.getString("tag");        }
    public String getLeader()  { return config.getString("leader");     }
    public Double getBalance() { return config.getDouble("balance", 0); }

    public boolean isRankLeader(String rank)  { return config.getBoolean("ranks." + rank + ".leader", false); }
    public boolean isRankDefault(String rank) { return config.getBoolean("ranks." + rank + ".default", false); }

    public ArrayList<GuildMember> getMembers() {

        Map<String, Object> members = config.getConfigurationSection("members").getValues(false);
        ArrayList<GuildMember> ret = new ArrayList<GuildMember>();

        for(Map.Entry<String, Object> entry : members.entrySet()) {
            if(entry.getValue() instanceof String) {
                String rankName = (String) entry.getValue() ;
                GuildRank rank = guild.getDefaultRank();
                for(GuildRank r : guild.getRanks()) {
                    if(r.getName().equals(rankName)) {
                        rank = r;
                    }
                }

                ret.add(new GuildMember(entry.getKey(), guild, rank));
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
        for(GuildRank gRank : getRanks()) {
            if(gRank.getName().equals(rank)) {
                return gRank;
            }
        }
        return null;
    }


    public String getHqWorld() {
        return config.getString("hq.world");
    }
    public int getHqX() {
        return config.getInt("hq.x");
    }
    public int getHqY() {
        return config.getInt("hq.y");
    }
    public int getHqZ() {
        return config.getInt("hq.z");
    }
    public double getHqYaw() {
        return config.getDouble("hq.yaw", 0);
    }
    public double getHqPitch() {
        return config.getDouble("hq.pitch", 0);
    }

    public void setHq(Location loc) {
        config.set("hq.world", loc.getWorld().getName());
        config.set("hq.x", (int) loc.getX());
        config.set("hq.y", (int) loc.getY());
        config.set("hq.z", (int) loc.getZ());
        config.set("hq.yaw", (double) loc.getYaw());
        config.set("hq.pitch", (double) loc.getPitch());
        saveFile();
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

    public void setBalance(Double balance) {
        config.set("balance", balance);
        saveFile();
    }

    public void setRankColour(String rank, ChatColor colour) {
        config.set("ranks." + rank + ".colour", colour.name());
        saveFile();
    }

    public void setRankPerm(String rank, RankPerm perm, boolean val) {
        if(val) {
            config.set("ranks." + rank + "." + perm.getPerm(), val);
        }else{
            config.set("ranks." + rank + "." + perm.getPerm(), null);
        }
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

    public void setRankName(String oldName, String newName) {
        ConfigurationSection ranks = config.getConfigurationSection("ranks");
        for(String key : ranks.getConfigurationSection(oldName).getKeys(true)) {
            ranks.set(newName + "." + key, ranks.get(oldName + "." + key));
        }
        ConfigurationSection members = config.getConfigurationSection("members");
        for(String key : members.getKeys(false)) {
            if(members.get(key).equals(oldName)) {
                members.set(key, newName);
            }
        }
        ranks.set(oldName, null);
        saveFile();

    }

    public boolean hasHq() {

        return config.getKeys(false).contains("hq");

    }

    public String[] getMOTD() {

        ConfigurationSection motdSection = config.getConfigurationSection("motd");
        if(motdSection == null) {
            return new String[0];
        }

        SortedMap<Integer, String> motd = new TreeMap<Integer, String>();

        for(String key : motdSection.getKeys(false)) {
            int line;
            try {
                line = Integer.parseInt(key);
            } catch (NumberFormatException e) {
                continue;
            }
            motd.put(line, motdSection.getString(key));
        }
        Collection<String> values = motd.values();
        return values.toArray(new String[values.size()]);

    }

    public void setMOTDLine(int line, String value) {
        if(value.equals("")) {
            value = null;
        }
        config.set("motd." + line, value);
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
