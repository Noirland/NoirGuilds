package me.zephirenz.noirguilds.databaseold;

import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;

public interface DatabaseManager {

    public ArrayList<Guild> getGuilds();

    public void createGuild(Guild guild);

    public void removeGuild(Guild guild);

    public void addMember(GuildMember member);

    public void removeMember(GuildMember member);

    public void addRank(GuildRank rank);

    public void removeRank(GuildRank rank);

    public void updateGuildTag(Guild guild, String tag);

    public void updateGuildName(Guild guild, String name);

    public void updateMemberRank(GuildMember mPromote, GuildRank newRank);

    public Location getHq(Guild guild);

    public void setHq(Guild guild, Location loc);

    public void setMOTDLine(Guild guild, int line, String val);

    public String[] getMOTD(Guild guild);

    public void saveAll();

    public void close();

    public void updateRankColour(GuildRank rank, ChatColor colour);

    public void updateRankName(GuildRank rank, String value);

    public void updateRankPerm(GuildRank rank, RankPerm perm, boolean val);

    public void setBalance(Guild guild, Double balance);
}
