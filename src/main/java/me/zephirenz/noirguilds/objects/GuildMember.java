package me.zephirenz.noirguilds.objects;

import me.zephirenz.noirguilds.enums.RankPerm;

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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildMember)) return false;
        GuildMember gPlayer = (GuildMember) obj;
        return gPlayer.getPlayer().equals(this.getPlayer());

    }
}
