package me.zephirenz.noirguilds.objects;

import me.zephirenz.noirguilds.enums.RankPerm;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class GuildRank {

    private Guild guild;
    private String name;
    private boolean leader = false;
    private boolean def = false;
    private Map<RankPerm, Boolean> perms;
    private ChatColor colour;

    public GuildRank(Guild guild, String name, Map<RankPerm, Boolean> perms, ChatColor colour) {
        this.guild = guild;
        this.name = name;
        this.colour = colour;
        if(perms != null) {
            this.perms = perms;
        }else{
            this.perms = new HashMap<RankPerm, Boolean>();
        }
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    public Map<RankPerm, Boolean> getPerms() {
        return perms;
    }

    public Boolean hasPerm(RankPerm perm) {
        if(perms.containsKey(perm)) {
            return perms.get(perm);
        }else{
            return false;
        }
    }

    public ChatColor getColour() {
        return colour;
    }

    public boolean isLeader() {
        return leader;
    }

    public boolean isDefault() {
        return def;
    }

    public void setPerm(RankPerm perm, boolean val) {
        perms.put(perm, val);
    }

    public void setLeader(boolean val) {
        leader = val;
    }

    public void setDefault(boolean val) {
        def = val;
    }

    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildRank)) return false;
        GuildRank rank = (GuildRank) obj;
        if(!rank.getName().equals(this.name)) return false;

        return true;
    }

    public void save() {

    }
}
