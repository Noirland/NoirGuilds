package me.zephirenz.noirguilds.objects;

import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.enums.RankPerm;
import nz.co.noirland.zephcore.Util;

import java.util.UUID;

public class GuildMember {

    private final UUID player;
    private GuildRank rank;

    private int kills;
    private int deaths;

    public GuildMember(UUID player, GuildRank rank, int kills, int deaths) {
        this.player = player;
        this.rank = rank;
        this.kills = kills;
        this.deaths = deaths;
        rank.getGuild().addMember(this);
    }

    public boolean hasPerm(RankPerm perm) {
        return rank.hasPerm(perm);
    }

    public UUID getPlayer() {
        return player;
    }

    public Guild getGuild() {
        return rank.getGuild();
    }

    public GuildRank getRank() {
        return rank;
    }

    public void setRank(GuildRank rank) {
        this.rank = rank;
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

    public void updateDB() {
        GuildsDatabase.inst().updateMember(this);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildMember)) return false;
        GuildMember gPlayer = (GuildMember) obj;
        return gPlayer.getPlayer().equals(this.getPlayer());
    }

    @Override
    public String toString() {
        return Util.player(player).getName();
    }
}
