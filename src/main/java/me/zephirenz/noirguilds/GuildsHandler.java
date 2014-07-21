package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.GuildsDatabase;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.UUID;

public class GuildsHandler {

    private final NoirGuilds plugin;
    private final GuildsDatabase db;
    private final ArrayList<Guild> guilds = new ArrayList<Guild>();
    private final ArrayList<GuildInviteTask> invites = new ArrayList<GuildInviteTask>();

    public GuildsHandler() {
        this.plugin = NoirGuilds.inst();
        this.db = GuildsDatabase.inst();
        updateGuildsList();
    }

    public void updateGuildsList() {
        guilds.clear();
        guilds.addAll(db.getGuilds());
    }

    public ArrayList<Guild> getGuilds() {
        return guilds;
    }

    public void addGuild(Guild guild) {
        guilds.add(guild);
        db.updateGuild(guild);
    }

    public void addRank(GuildRank rank) {
        db.updateRank(rank);
    }

    public void addMember(GuildMember member) {
        db.updateMember(member);
    }

    public void removeGuild(Guild guild) {
        guilds.remove(guild);
        for(GuildInviteTask task : getInvites()) {
            if(task.getData().getGuild() == guild) {
                task.run();
                task.cancel();
            }
        }
        db.removeGuild(guild);
    }

    public void removeRank(GuildRank rank) {
        for(GuildMember member : rank.getGuild().getMembers()) {
            if(member.getRank() == rank){
                member.setRank(rank.getGuild().getDefaultRank());
                OfflinePlayer p = Util.player(member.getPlayer());
                if(p.isOnline()) {
                    plugin.sendMessage(p.getPlayer(), String.format(Strings.RANK_DELETE_RANK_DELETED, member.getRank().getColour() + member.getRank().getName()));
                }
            }
        }
        db.removeRank(rank);
    }

    public void removeGuildMember(GuildMember member) {
        db.removeMember(member);

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

    public GuildMember getGuildMember(UUID player) {
        for(Guild guild : guilds) {
            for(GuildMember member : guild.getMembers()) {
                if(member.getPlayer().equals(player)) {
                    return member;
                }
            }
        }
        return null;
    }

    public ArrayList<GuildInviteTask> getInvites() {
        return invites;
    }

    public void addInvite(GuildInviteTask task) {
        invites.add(task);
    }

    public void removeInvite(GuildInviteTask task) {
        invites.remove(task);
    }
}
