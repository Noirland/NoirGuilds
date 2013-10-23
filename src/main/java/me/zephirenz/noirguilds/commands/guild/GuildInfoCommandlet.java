package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GuildInfoCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildInfoCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    /**
     *  The commandlet for showing guild info.
     *  Usage: /guild accept [player]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(args.length != 1) {
            plugin.sendMessage(sender, "You must specify a guild name or tag to inspect.");
            return;
        }

        String gName = args[0];

        Guild guild = null;
        for(Guild g : gHandler.getGuilds()) {
            if(g.getTag().equalsIgnoreCase(gName) || g.getName().equalsIgnoreCase(gName)) {
                guild = g;
                break;
            }
        }

        if(guild == null) {
            plugin.sendMessage(sender, "That guild does not exist.");
            return;
        }

        StringBuilder membersString = new StringBuilder(ChatColor.BLUE + "Members" + ChatColor.GRAY + "[" + guild.getMembers().size() + "]" + ChatColor.BLUE + ": ");

        String delim = ChatColor.RESET.toString();
        for(GuildMember member : guild.getMembers()) {
            String memberString = (plugin.getServer().getOfflinePlayer(member.getPlayer()).isOnline() ? ChatColor.GREEN : "") + member.getPlayer();
            membersString.append(delim).append(memberString);
            delim = ChatColor.WHITE + ", ";
        }

        String tagString = ChatColor.GRAY + "[" + guild.getTag() + "]";
        String titleString = ChatColor.RED + "====== " + ChatColor.WHITE + guild.getName() + " " + tagString + ChatColor.RED + " ======";

        sender.sendMessage(titleString);
        sender.sendMessage(ChatColor.BLUE + "Leader: " + ChatColor.WHITE + guild.getLeader());
        sender.sendMessage(membersString.toString());
        sender.sendMessage(ChatColor.RED + StringUtils.repeat("=", ChatColor.stripColor(titleString).length()-3));
    }

}
