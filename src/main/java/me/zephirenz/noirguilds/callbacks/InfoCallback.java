package me.zephirenz.noirguilds.callbacks;

import com.google.common.util.concurrent.FutureCallback;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.zephcore.Util;
import nz.co.noirland.zephcore.callbacks.GetNamesCallback;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class InfoCallback {

    public InfoCallback(CommandSender sender, Guild guild) {
        List<UUID> members = new ArrayList<UUID>();

        for(GuildMember member : guild.getMembers()) {
            members.add(member.getPlayer());
        }

        // Wrap around the callback, so we can generate a list of uuids.
        new GetNamesCallback(new InfoResult(sender, guild), members);
    }

}

class InfoResult implements FutureCallback<Map<UUID, String>> {
    private final CommandSender to;
    private final Guild guild;

    public InfoResult(CommandSender to, Guild guild) {
        this.to = to;
        this.guild = guild;
    }

    @Override
    public void onSuccess(Map<UUID, String> names) {
        String membersString = ChatColor.BLUE + "Members" + ChatColor.GRAY + "[" + guild.getMembers().size() + "/" + guild.getMemberLimit() + "]" + ChatColor.BLUE + ": ";
        String headerString = ChatColor.RED + "====== " + ChatColor.WHITE + guild.getName() + " " + ChatColor.GRAY + "[" + guild.getTag() + "]" + ChatColor.RED + " ======";
        String footerString = ChatColor.RED + StringUtils.repeat("=", ChatColor.stripColor(headerString).length() - 3);

        EcoManager eco = EcoManager.inst();
        String balString = ChatColor.BLUE + "Bank: " + ChatColor.WHITE + eco.format(guild.getBalance());

        List<String> members = new ArrayList<String>();
        List<String> leaders = new ArrayList<String>();
        for(UUID player : names.keySet()) {
            members.add((Util.player(player).isOnline() ? ChatColor.GREEN.toString() : "") + names.get(player));
        }
        for(GuildMember member : guild.getMembersByRank(guild.getLeaderRank())) {
            if(names.containsKey(member.getPlayer())) {
                leaders.add(names.get(member.getPlayer()));
            }
        }

        membersString = Util.concatenate(membersString, members, ChatColor.WHITE.toString(), ChatColor.RESET + ", " + ChatColor.WHITE);
        String leaderString = Util.concatenate(ChatColor.BLUE + "Leader: " + ChatColor.WHITE, leaders, "", ", ");

        to.sendMessage(headerString);
        to.sendMessage(leaderString);
        to.sendMessage(balString);
        to.sendMessage(membersString);
        to.sendMessage(footerString);
    }

    @Override
    public void onFailure(Throwable throwable) {
        to.sendMessage(ChatColor.DARK_RED + "Internal error occurred!");
        NoirGuilds.debug().warning("Could not get guild info for " + guild.getName(), throwable);
    }
}
