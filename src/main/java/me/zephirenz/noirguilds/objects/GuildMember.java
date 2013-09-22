package me.zephirenz.noirguilds.objects;

public class GuildMember {

    private String player;
    private Guild guild;
    private GuildRank rank;

    public GuildMember(String player) {
        this(player, null);
    }

    public GuildMember(String player, Guild guild) {
        this.player = player;
        this.guild = guild;
    }

    public GuildMember(String player, Guild guild, GuildRank rank) {
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

    public void setRank(GuildRank rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof GuildMember)) return false;
        GuildMember gPlayer = (GuildMember) obj;
        if(!(gPlayer.getPlayer().equals(this.getPlayer()))) return false;

        return true;
    }

}
