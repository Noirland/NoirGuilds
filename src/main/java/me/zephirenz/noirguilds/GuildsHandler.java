package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class GuildsHandler {

    private final GuildsDatabase db;
    private final List<Guild> guilds = new ArrayList<Guild>();
    private final List<GuildInviteTask> invites = new ArrayList<GuildInviteTask>();

    public GuildsHandler() {
        this.db = GuildsDatabase.inst();
        guilds.addAll(db.getGuilds());
    }

    public void save() {
        for(Guild guild : guilds) {
            guild.updateDB();
            for(GuildMember member : guild.getMembers()) {
                member.updateDB();
            }
            for(GuildRank rank : guild.getRanks()) {
                rank.updateDB();
            }
        }
    }

    public Collection<Guild> getGuilds() {
        return guilds;
    }

    public void addGuild(Guild guild) {
        guilds.add(guild);
    }

    public void removeGuild(Guild guild) {
        for(GuildInviteTask task : getInvites()) {
            if(task.getData().getGuild() == guild) {
                task.run();
                task.cancel();
            }
        }
        guilds.remove(guild);
    }

    public Guild getGuildByName(String name) {
        for(Guild guild : guilds) {
            if(guild.getName().equalsIgnoreCase(name)) {
                return guild;
            }
        }
        return null;
    }

    public Guild getGuildByTag(String tag) {
        for(Guild guild : guilds) {
            if(guild.getTag().equalsIgnoreCase(tag)) {
                return guild;
            }
        }
        return null;
    }

    public GuildMember getMember(UUID player) {
        for(Guild guild : guilds) {
            for(GuildMember member : guild.getMembers()) {
                if(member.getPlayer().equals(player)) {
                    return member;
                }
            }
        }
        return null;
    }

    public GuildMember getMember(OfflinePlayer player) {
        return getMember(player.getUniqueId());
    }

    public GuildMember getMember(String name) {
        return getMember(Util.player(name));
    }

    public Collection<GuildInviteTask> getInvites() {
        return invites;
    }

    public void addInvite(GuildInviteTask task) {
        invites.add(task);
    }

    public void removeInvite(GuildInviteTask task) {
        invites.remove(task);
    }

    public int createGuildID() {
        while(true) { // This should be safe in thread, over 60,000 possible IDs
            int id = Util.createRandomHex(4);
            boolean found = false;
            for(Guild guild : guilds) {
                if(guild.getId() == id) {
                    found = true;
                    break;
                }
            }
            if(!found) return id;
        }
    }

    public int createRankID() {
        while(true) { // This should be safe in thread, over 65,000 possible IDs
            int id = Util.createRandomHex(4);
            boolean found = false;
            for(Guild guild : guilds) {
                for(GuildRank rank : guild.getRanks()) {
                    if(rank.getId() == id) {
                        found = true;
                        break;
                    }
                }
                if(found) break;
            }
            if(!found) return id;
        }
    }
}
