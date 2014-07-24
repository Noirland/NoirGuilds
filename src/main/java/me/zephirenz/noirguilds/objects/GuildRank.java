package me.zephirenz.noirguilds.objects;

import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.enums.RankPerm;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GuildRank {

    private final int id;

    private final Guild guild;
    private String name;
    private final Map<RankPerm, Boolean> perms;
    private ChatColor colour;

    private boolean leader = false;
    private boolean def = false;

    public GuildRank(int id, Guild guild, String name, Map<RankPerm, Boolean> perms, ChatColor colour) {
        this.id = id;
        this.guild = guild;
        this.name = name;
        this.colour = colour;
        if(perms != null) {
            this.perms = perms;
        }else{
            this.perms = new HashMap<RankPerm, Boolean>(RankPerm.defaults);
        }
        guild.addRank(this);
    }

    public void sendMessage(String msg, boolean prefix) {
        for(GuildMember member : guild.getMembers()) {
            if(!(member.getRank().equals(this))) {
                return;
            }
            OfflinePlayer player = Util.player(member.getPlayer());
            if(player.isOnline()) {
                player.getPlayer().sendMessage((prefix ? Strings.MESSAGE_PREFIX : "") + msg);
            }
        }
    }

    public void remove() {
        Collection<GuildMember> changed = guild.getMembersByRank(this);
        for(GuildMember member : changed) {
            member.setRank(guild.getDefaultRank());
            member.updateDB();
        }
        sendMessage(String.format(Strings.RANK_DELETE_RANK_DELETED, guild.getDefaultRank().getColour() + guild.getDefaultRank().getName()), true);
    }

    public Boolean hasPerm(RankPerm perm) {
        return leader || (perms.containsKey(perm) ? perms.get(perm) : false);
    }

    public void setLeader() {
        leader = true;
    }

    public void setDefault() {
        def = true;
    }

    public boolean isLeader() {
        return leader;
    }

    public boolean isDefault() {
        return def;
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

    public ChatColor getColour() {
        return colour;
    }

    public void setPerm(RankPerm perm, boolean val) {
        perms.put(perm, val);
    }

    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void updateDB() {
        GuildsDatabase.inst().updateRank(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildRank)) return false;
        GuildRank rank = (GuildRank) obj;
        return rank.getName().equals(this.name);
    }

    @Override
    public String toString() {
        return colour + name;
    }
}
