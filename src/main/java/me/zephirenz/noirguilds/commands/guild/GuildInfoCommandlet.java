package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildInfoCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildInfoCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    /**
     *  The commandlet for showing guild info.
     *  Usage: /guild info (guild)
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        Guild guild = null;

        if(args.length >= 1) {
            String gName = args[0];

            guild = gHandler.getGuildByName(gName);
            if(guild == null) {
                guild = gHandler.getGuildByTag(gName);
            }

            if(guild == null) {
                plugin.sendMessage(sender, GUILD_NOT_EXISTS);
                return;
            }
        }else{
            if(!(sender instanceof Player)) {
                plugin.sendMessage(sender, NO_CONSOLE);
                return;
            }
            Player player = (Player) sender;
            GuildMember gMember = gHandler.getGuildMember(player.getName());
            if(gMember == null) {
                plugin.sendMessage(sender, GUILD_LIST_NO_GUILD);
                return;
            }
            guild = gMember.getGuild();
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
