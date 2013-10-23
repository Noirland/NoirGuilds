package me.zephirenz.noirguilds.objects;

import java.util.ArrayList;

public class Guild {

    private String name;
    private String tag;
    private String leader;

    private ArrayList<GuildRank> ranks = new ArrayList<GuildRank>();
    private ArrayList<GuildMember> members = new ArrayList<GuildMember>();

    private String[] motd;

    public Guild(String name, String tag, String leader) {
        this.name = name;
        this.tag = tag;
        this.leader = leader;
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

    public boolean isMember(String player) {
        for(GuildMember gPlayer: members) {
            if(gPlayer.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMember(GuildMember gPlayer) {
        return isMember(gPlayer.getPlayer());
    }

    public void addGuildMember(GuildMember member) {

        members.add(member);

    }

    public void removeGuildMember(GuildMember member) {

        if(members.contains(member)) {
            members.remove(member);
            save();
        }

    }

    public void setMembers(ArrayList<GuildMember> members) {
        this.members = members;
        save();
    }

    public void addRank(GuildRank rank) {
        ranks.add(rank);
        save();
    }

    public void removeRank(GuildRank rank) {
        if(ranks.contains(rank)) {
            ranks.remove(rank);
            save();
        }
    }

    public void setRanks(ArrayList<GuildRank> ranks) {
        this.ranks = ranks;
        save();
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

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public GuildRank getDefaultRank() {
        for(GuildRank rank : getRanks()) {
            if(rank.isDefault()) {
                return rank;
            }
        }
        return null;
    }

    public String[] getMotd() {
        return motd;
    }

    public void setMotd(String[] motd) {
        this.motd = motd;
    }

    public void save() {



    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Guild)) return false;
        Guild guild = (Guild) obj;
        return guild.getName().equals(this.getName());

    }
}
