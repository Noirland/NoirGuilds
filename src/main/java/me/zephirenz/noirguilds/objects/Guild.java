package me.zephirenz.noirguilds.objects;

import java.util.ArrayList;

public class Guild {

    private String guild;

    private ArrayList<GuildRank> ranks;
    private ArrayList<GuildPlayer> members;

    public Guild(String name, String tag) {

    }

    public void setGuildName(String name) {
        this.guild = name;
    }

    public String getGuildName() {
        return guild;
    }

    public GuildPlayer[] getGuildMemebers() {
        return null;
    }

    public boolean isMember(String player) {
        for(GuildPlayer gPlayer: members) {
            if(gPlayer.getPlayer() == player) {
                return true;
            }
        }
        return false;
    }

    public boolean isMember(GuildPlayer gPlayer) {
        return isMember(gPlayer.getPlayer());
    }

    public void addGuildMember(GuildPlayer member) {

          members.add(member);

    }

    public void removeGuildMember(GuildPlayer member) {

        if(members.contains(member)) {
            members.remove(member);
        }

    }

    public void setMembers(ArrayList<GuildPlayer> members) {
        this.members = members;
    }

    public void addRank(GuildRank rank) {
        ranks.add(rank);
    }

    public void removeRank(GuildRank rank) {
        if(ranks.contains(rank)) {
            ranks.remove(rank);
        }
    }

    public void setRanks(ArrayList<GuildRank> ranks) {
        this.ranks = ranks;
    }

    public void saveGuild() {



    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Guild)) return false;
        Guild guild = (Guild) obj;
        if(!guild.getGuildName().equals(this.getGuildName())) return false;

        return true;
    }

}
