package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import me.zephirenz.noirguilds.tasks.GuildInviteTask;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GuildsHandler {

    private final NoirGuilds plugin;
    private final DatabaseManager dbManager;
    private final ArrayList<Guild> guilds = new ArrayList<Guild>();
    private final ArrayList<GuildInviteTask> invites = new ArrayList<GuildInviteTask>();

    public GuildsHandler() {
        this.plugin = NoirGuilds.inst();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
        updateGuildsList();
    }

    public void updateGuildsList() {

         for(Guild guildA : dbManager.getGuilds()) {
             boolean inArray = false;
             for(Guild guildB : guilds) {
                 if(guildA.equals(guildB)) {
                     inArray = true;
                 }
             }
             if(!inArray) {
                 guilds.add(guildA);
             }
         }
    }

    public ArrayList<Guild> getGuilds() {
        return guilds;
    }

    public void addGuild(Guild guild) {
        guilds.add(guild);
        dbManager.createGuild(guild);
    }

    public void addRank(GuildRank rank) {
        dbManager.addRank(rank);
    }

    public void addMember(GuildMember member) {
        dbManager.addMember(member);
    }

    public void removeGuild(Guild guild) {
        guilds.remove(guild);
        for(GuildInviteTask task : getInvites()) {
            if(task.getData().getGuild() == guild) {
                task.run();
                task.cancel();
            }
        }
        plugin.sendGlobalMessage(guild.getName() + " has been disbanded.");
        dbManager.removeGuild(guild);
    }

    public void removeRank(GuildRank rank) {
        for(GuildMember member : rank.getGuild().getMembers()) {
            if(member.getRank() == rank){
                member.setRank(rank.getGuild().getDefaultRank());
                Player p = plugin.getServer().getPlayer(member.getPlayer());
                if(p != null) {
                    plugin.sendMessage(p, "Your rank has been deleted, you are now " + member.getRank().getColour() + member.getRank().getName());
                }
            }
        }
        dbManager.removeRank(rank);
    }

    public void removeGuildMember(GuildMember member) {
        dbManager.removeMember(member);

    }

    public Guild getGuild(String name) {
        for(Guild guild : guilds) {
            if(guild.getName().equalsIgnoreCase(name)) {
                return guild;
            }
        }
        return null;
    }

    public GuildMember getGuildMember(String player) {
        for(Guild guild : guilds) {
            for(GuildMember member : guild.getMembers()) {
                if(member.getPlayer().equals(player)) {
                    return member;
                }
            }
        }
        return null;
    }

    public void sendMessageToGuild(Guild guild, String msg) {
        for(GuildMember gPlayer : guild.getMembers()) {
            Player player = plugin.getServer().getPlayer(gPlayer.getPlayer());
            if(player != null) {
                player.sendMessage(msg);
            }
        }
    }

    public void sendMessageToRank(Guild guild, GuildRank rank, String msg) {
        for(GuildMember gPlayer : guild.getMembers()) {
            if(!(gPlayer.getRank().equals(rank))) {
                return;
            }
            Player player = plugin.getServer().getPlayer(gPlayer.getPlayer());
            if(player != null) {
                player.sendMessage(msg);
            }
        }
    }

    public boolean hasPerm(GuildMember member, RankPerm perm) {
        return hasPerm(member.getRank(), perm);
    }

    public boolean hasPerm(GuildRank rank, RankPerm perm) {
        if(rank.isLeader()) {
            return true;
        }else{
            return rank.hasPerm(perm);
        }
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
