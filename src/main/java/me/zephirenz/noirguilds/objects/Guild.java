package me.zephirenz.noirguilds.objects;

import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.database.GuildsDatabase;
import nz.co.noirland.zephcore.Util;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Guild {

    private ArrayList<GuildRank> ranks = new ArrayList<GuildRank>();

    private ArrayList<GuildMember> members = new ArrayList<GuildMember>();

    final private int id;
    private String name;
    private String tag;
    private List<String> motd;
    private Double balance;
    private Location hq;
    private long kills;
    private long deaths;

    public Guild(int id, String name, String tag, double balance, long kills, long deaths, List<String> motd, Location hq) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.balance = balance;
        this.kills = kills;
        this.deaths = deaths;
        this.motd = motd;
        this.hq = hq;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String msg, boolean prefix) {
        for(GuildMember member : getMembers()) {
            OfflinePlayer player = Util.player(member.getPlayer());
            if(player.isOnline()) {
                player.getPlayer().sendMessage((prefix ? Strings.MESSAGE_PREFIX : "") + msg);
            }
        }
    }

    public GuildRank getLeaderRank() {
        for(GuildRank rank : ranks) {
            if(rank.isLeader()) return rank;
        }
        return null;
    }

    public GuildRank getDefaultRank() {
        for(GuildRank rank : ranks) {
            if(rank.isDefault()) return rank;
        }
        return null;
    }

    public Collection<GuildMember> getMembersByRank(GuildRank rank) {
        Collection<GuildMember> ret = new HashSet<GuildMember>();
        for(GuildMember member : ret) {
            if(member.getRank() == rank) ret.add(member);
        }
        return ret;
    }

    public int getId() {
        return id;
    }

    public ArrayList<GuildMember> getMembers() {
        return members;
    }

    public void addMember(GuildMember member) {
        members.add(member);
    }

    public void addMembers(Collection<GuildMember> members) {
        this.members.addAll(members);
    }

    public void removeMember(GuildMember member) {
        members.remove(member);
    }

    public ArrayList<GuildRank> getRanks() {
        return ranks;
    }

    public void addRank(GuildRank rank) {
        ranks.add(rank);
    }

    public void addRanks(Collection<GuildRank> ranks) {
        this.ranks.addAll(ranks);
    }

    public void removeRank(GuildRank rank) {
        rank.remove();
        ranks.remove(rank);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getMotd() {
        return motd;
    }

    public void setMotd(List<String> motd) {
        this.motd = motd;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Location getHQ() {
        return hq;
    }

    public void setHQ(Location hq) {
        this.hq = hq;
    }

    public long getKills() {
        return kills;
    }

    public void setKills(long kills) {
        this.kills = kills;
    }

    public long getDeaths() {
        return deaths;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public void updateDB() {
        GuildsDatabase.inst().updateGuild(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Guild)) return false;
        Guild guild = (Guild) obj;
        return guild.getName().equals(this.getName());

    }

    @Override
    public String toString() {
        return name;
    }
}
