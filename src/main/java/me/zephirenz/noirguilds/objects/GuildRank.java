package me.zephirenz.noirguilds.objects;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class GuildRank {

    private Guild guild;
    private String name;
    private Map<String, Boolean> perms;
    private ChatColor colour;

    public GuildRank(Guild guild, String name, Map<String, Boolean> perms, ChatColor colour) {
        this.guild = guild;
        this.name = name;
        this.colour = colour;
        if(perms != null) {
            this.perms = perms;
        }else{
            this.perms = new HashMap<String, Boolean>();
        }
    }

    public String getName() {
        return name;
    }

    public Guild getGuild() {
        return guild;
    }

    public Map<String, Boolean> getPerms() {
        return perms;
    }

    public Boolean getPerm(String perm) {
        if(perms.containsKey("leader") && perms.get("leader")) {
            return true;
        }else if(perms.containsKey(perm)) {
            return perms.get(perm);
        }else{
            return false;
        }
    }

    public ChatColor getColour() {
        return colour;
    }

    public void setPerm(String perm, boolean val) {
        perms.put(perm, val);
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
