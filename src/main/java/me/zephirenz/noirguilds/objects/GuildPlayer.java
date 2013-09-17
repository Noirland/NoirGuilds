package me.zephirenz.noirguilds.objects;

import org.bukkit.entity.Player;

public class GuildPlayer {

    private String player;
    private Guild guild;
    private GuildRank rank;

    public GuildPlayer(String player) {
        this(player, null);
    }

    public GuildPlayer(String player, Guild guild) {
        this.player = player;
        this.guild = guild;
    }

    public GuildPlayer(String player, Guild guild, GuildRank rank) {
        this.player = player;
        this.guild = guild;
        this.rank = rank;
    }

    public String getPlayer() {
        return player;
    }

    public Guild getGuild() {
        return guild;
    }

    public GuildRank getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildPlayer)) return false;
        GuildPlayer gPlayer = (GuildPlayer) obj;
        if(!(gPlayer.getPlayer().equals(this.getPlayer()))) return false;

        return true;
    }

}
