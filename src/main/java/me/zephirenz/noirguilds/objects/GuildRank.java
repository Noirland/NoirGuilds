package me.zephirenz.noirguilds.objects;

import org.bukkit.ChatColor;

import java.util.Map;

public class GuildRank {

    private Guild guild;
    private String name;
    private Map<String, Boolean> perms;
    private ChatColor colour;

    public GuildRank(Guild guild, String name, Map<String, Boolean> perms, ChatColor colour) {
        this.guild = guild;
        this.name = name;
        this.perms = perms;
        this.colour = colour;
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
        if(perms.containsKey(perm)) {
            return perms.get(perm);
        }else{
            return null;
        }
    }

    public ChatColor getColour() {
        return colour;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildRank)) return false;
        GuildRank rank = (GuildRank) obj;
        if(!rank.getName().equals(this.name)) return false;

        return true;
    }


}
