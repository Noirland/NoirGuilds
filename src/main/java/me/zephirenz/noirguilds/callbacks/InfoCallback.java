package me.zephirenz.noirguilds.callbacks;

import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class InfoCallback extends Callback {

    Guild guild;
    CommandSender sender;
    Map<UUID, String> members = new HashMap<UUID, String>();
    Map<UUID, String> leaders = new HashMap<UUID, String>();

    String titleString;
    String membersString;
    String balString;

    public InfoCallback(CommandSender sender, Guild guild) {
        super(NoirGuilds.inst());
        this.guild = guild;
        this.sender = sender;

        List<UUID> members = new ArrayList<UUID>();
        List<UUID> leaders = new ArrayList<UUID>();
        for(GuildMember member : guild.getMembers()) {
            members.add(member.getPlayer());
        }
        for(GuildMember member : guild.getMembersByRank(guild.getLeaderRank())) {
            leaders.add(member.getPlayer());
        }

        // Avoid stats changing after thread is complete
        membersString = ChatColor.BLUE + "Members" + ChatColor.GRAY + "[" + guild.getMembers().size() + "]" + ChatColor.BLUE + ": ";
        titleString = ChatColor.RED + "====== " + ChatColor.WHITE + guild.getName() + " " + ChatColor.GRAY + "[" + guild.getTag() + "]" + ChatColor.RED + " ======";
        EcoManager eco = EcoManager.inst();
        balString = ChatColor.BLUE + "Bank: " + ChatColor.WHITE + eco.format(guild.getBalance());

        new InfoThread(members, leaders);
    }

    @Override
    public void run() {
        List<String> temp = new ArrayList<String>();
        for(UUID player : this.members.keySet()) {
            temp.add((Util.player(player).isOnline() ? ChatColor.GREEN.toString() : "") + this.members.get(player));
        }
        membersString = Util.concatenate(membersString, temp, ChatColor.WHITE.toString(), ChatColor.RESET + ", " + ChatColor.WHITE);
        String leaderString = Util.concatenate(ChatColor.BLUE + "Leader: " + ChatColor.WHITE, leaders.values(), "", ", ");

        sender.sendMessage(titleString);
        sender.sendMessage(leaderString);

        sender.sendMessage(balString);
        sender.sendMessage(membersString);
        sender.sendMessage(ChatColor.RED + StringUtils.repeat("=", ChatColor.stripColor(titleString).length() - 3));
    }

    private class InfoThread implements Runnable {

        private List<UUID> members;
        private List<UUID> leaders;

        private InfoThread(List<UUID> members, List<UUID> leaders) {
            this.members = members;
            this.leaders = leaders;
            new Thread(this, "NoirGuilds-InfoThread").start();
        }

        @Override
        public void run() {
            for(UUID uuid : members) {
                InfoCallback.this.members.put(uuid, Util.name(uuid));
            }
            for(UUID uuid : leaders) {
                InfoCallback.this.leaders.put(uuid, Util.name(uuid));
            }
            schedule();
        }
    }

}
