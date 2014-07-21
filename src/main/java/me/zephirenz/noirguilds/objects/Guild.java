package me.zephirenz.noirguilds.objects;

import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collection;

public class Guild {

    final private int id;

    private String name;
    private String tag;

    private ArrayList<GuildRank> ranks = new ArrayList<GuildRank>();
    private ArrayList<GuildMember> members = new ArrayList<GuildMember>();

    private String[] motd;

    private Double balance;

    private int kills;
    private int deaths;

    public Guild(int id, String name, String tag, double balance, int kills, int deaths, String[] motd) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.balance = balance;
        this.kills = kills;
        this.deaths = deaths;
        this.motd = motd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<GuildMember> getMembers() {
        return members;
    }

    public void sendMessage(String msg) {
        for(GuildMember member : getMembers()) {
            OfflinePlayer player = Util.player(member.getPlayer());
            if(player.isOnline()) {
                player.getPlayer().sendMessage(msg);
            }
        }
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

    public void addRank(GuildRank rank) {
        ranks.add(rank);
    }

    public void addRanks(Collection<GuildRank> ranks) {
        this.ranks.addAll(ranks);
    }

    public void removeRank(GuildRank rank) {
        ranks.remove(rank);
    }

    public ArrayList<GuildRank> getRanks() {
        return ranks;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String[] getMotd() {
        return motd;
    }

    public void setMotd(String[] motd) {
        this.motd = motd;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Guild)) return false;
        Guild guild = (Guild) obj;
        return guild.getName().equals(this.getName());

    }
}
