package me.zephirenz.noirguilds.database;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.config.PluginConfig;
import me.zephirenz.noirguilds.database.queries.guild.AddGuildQuery;
import me.zephirenz.noirguilds.database.queries.guild.GetAllGuildsQuery;
import me.zephirenz.noirguilds.database.queries.guild.RemoveGuildQuery;
import me.zephirenz.noirguilds.database.queries.guild.UpdateGuildQuery;
import me.zephirenz.noirguilds.database.queries.member.AddMemberQuery;
import me.zephirenz.noirguilds.database.queries.member.GetMembersByRankQuery;
import me.zephirenz.noirguilds.database.queries.member.RemoveMemberQuery;
import me.zephirenz.noirguilds.database.queries.member.UpdateMemberQuery;
import me.zephirenz.noirguilds.database.queries.rank.AddRankQuery;
import me.zephirenz.noirguilds.database.queries.rank.GetRanksByGuildQuery;
import me.zephirenz.noirguilds.database.queries.rank.RemoveRankQuery;
import me.zephirenz.noirguilds.database.queries.rank.UpdateRankQuery;
import me.zephirenz.noirguilds.database.schema.Schema1;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.MySQLDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.SQLException;
import java.util.*;

public class GuildsDatabase extends MySQLDatabase {

    private static GuildsDatabase inst;

    private static PluginConfig config = PluginConfig.inst();

    public static GuildsDatabase inst() {
        if(inst == null) {
            return new GuildsDatabase();
        }
        return inst;
    }

    private GuildsDatabase() {
        inst = this;
        schemas.put(1, new Schema1());
    }

    @Override
    public Debug debug() {
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
    public String getPrefix() {
        return config.getDBPrefix();
    }

    /**
     * Gets all guilds from the database, including members and ranks (using their respective functions).
     * @return A list of all Guilds in the database
     */
    public Collection<Guild> getGuilds() {
        List<Map<String, Object>> rawGuilds;
        List<Guild> guilds = new ArrayList<Guild>();
        try {
            rawGuilds = new GetAllGuildsQuery().executeQuery();
        } catch (SQLException e) {
            debug().disable("Unable to load guilds from database!");
            return guilds;
        }

        for(Map<String, Object> rawGuild : rawGuilds) {
            int id = (Integer) rawGuild.get("id");
            String name = (String) rawGuild.get("name");
            String tag = (String) rawGuild.get("tag");
            Double balance = (Double) rawGuild.get("balance");
            int kills = (Integer) rawGuild.get("kills");
            int deaths = (Integer) rawGuild.get("deaths");
            JSONArray motd = new JSONArray();
            try {
                motd = ((JSONArray) new JSONParser().parse((String) rawGuild.get("motd")));
            } catch (ParseException e) {
                debug().warning("Could not parse motd for guild: " + name, e);
            }
            Location hq = null;
            if(rawGuild.get("hq") != null) {
                try {
                    JSONObject hqJSON = ((JSONObject) new JSONParser().parse((String) rawGuild.get("hq")));
                    World world = Bukkit.getWorld((String) hqJSON.get("world"));
                    double x = (Double) hqJSON.get("x");
                    double y = (Double) hqJSON.get("y");
                    double z = (Double) hqJSON.get("z");
                    float yaw = (Float) hqJSON.get("yaw");
                    float pitch = (Float) hqJSON.get("pitch");
                    hq = new Location(world, x, y, z, yaw, pitch);
                } catch (Exception ignored) {}
            }
            //TODO: Update to add HQs (maybe via JSON)
            Guild guild = new Guild(id, name, tag, balance, kills, deaths, motd, hq);
            guilds.add(guild);
            for(GuildRank rank : getRanks(guild)) {
                getMembers(rank);
            }
        }
        return guilds;
    }

    public Collection<GuildRank> getRanks(Guild guild) {
        List<Map<String, Object>> rawRanks;
        List<GuildRank> ranks = new ArrayList<GuildRank>();
        try {
            rawRanks = new GetRanksByGuildQuery(guild).executeQuery();
        } catch (SQLException e) {
            debug().disable("Unable to load ranks of " + guild.getName() + " from database!");
            return ranks;
        }

        for(Map<String, Object> rawRank : rawRanks) {
            Map<RankPerm, Boolean> perms = new HashMap<RankPerm, Boolean>();
            try {
                JSONObject permsJSON = (JSONObject) new JSONParser().parse((String) rawRank.get("perms"));

                for(Object key : permsJSON.keySet()) {
                    perms.put(RankPerm.get((String) key), (Boolean) permsJSON.get(key));
                }
                if(perms.isEmpty()) perms = null;
            } catch (ParseException e) {
                perms = null;
            }
            int id = (Integer) rawRank.get("id");
            ChatColor colour = ChatColor.valueOf((String) rawRank.get("colour"));
            String name = (String) rawRank.get("name");
            ranks.add(new GuildRank(id, guild, name, perms, colour));
        }
        return ranks;
    }

    public Collection<GuildMember> getMembers(GuildRank rank) {
        List<Map<String, Object>> rawMembers;
        List<GuildMember> members = new ArrayList<GuildMember>();
        try {
            rawMembers = new GetMembersByRankQuery(rank).executeQuery();
        } catch (SQLException e) {
            debug().disable("Unable to load members of " + rank.getGuild().getName() + " from database!");
            return members;
        }

        for(Map<String, Object> rawMember : rawMembers) {
            UUID uuid = UUID.fromString((String) rawMember.get("uuid"));
            int kills = (Integer) rawMember.get("kills");
            int deaths = (Integer) rawMember.get("deaths");
            members.add(new GuildMember(uuid, rank, kills, deaths));
        }
        return members;
    }

    public void addGuild(Guild guild) {
        new AddGuildQuery(guild).executeAsync();
    }

    public void updateGuild(Guild guild) {
        new UpdateGuildQuery(guild).executeAsync();
    }

    public void removeGuild(Guild guild) {
        new RemoveGuildQuery(guild).executeAsync();
    }

    public void addMember(GuildMember member) {
        new AddMemberQuery(member).executeAsync();
    }

    public void updateMember(GuildMember member) {
        new UpdateMemberQuery(member).executeAsync();
    }

    public void removeMember(GuildMember member) {
        new RemoveMemberQuery(member).executeAsync();
    }

    public void addRank(GuildRank rank) {
        new AddRankQuery(rank).executeAsync();
    }

    public void updateRank(GuildRank rank) {
        new UpdateRankQuery(rank).executeAsync();
    }

    public void removeRank(GuildRank rank) {
        new RemoveRankQuery(rank).executeAsync();
    }

}
