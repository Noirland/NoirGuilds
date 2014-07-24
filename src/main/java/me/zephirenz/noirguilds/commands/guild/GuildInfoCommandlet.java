package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.zephcore.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
        Guild guild;

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
            GuildMember member = gHandler.getMember(player.getName());
            if(member == null) {
                plugin.sendMessage(sender, GUILD_LIST_NO_GUILD);
                return;
            }
            guild = member.getGuild();
        }

        String membersString = ChatColor.BLUE + "Members" + ChatColor.GRAY + "[" + guild.getMembers().size() + "]" + ChatColor.BLUE + ": ";

        List<String> members = new ArrayList<String>();
        for(GuildMember member : guild.getMembers()) {
            members.add((Util.player(member.getPlayer()).isOnline() ? ChatColor.GREEN.toString() : "") + member.getPlayer());
        }
        membersString = Util.concatenate(membersString, members, ChatColor.RESET.toString(), ChatColor.WHITE + ", ");

        String tagString = ChatColor.GRAY + "[" + guild.getTag() + "]";
        String titleString = ChatColor.RED + "====== " + ChatColor.WHITE + guild.getName() + " " + tagString + ChatColor.RED + " ======";

        sender.sendMessage(titleString);
        sender.sendMessage(Util.concatenate(ChatColor.BLUE + "Leader: " + ChatColor.WHITE, guild.getMembersByRank(guild.getLeaderRank()), "", ", "));
        EcoManager eco = EcoManager.inst();
        String bal = eco.format(guild.getBalance());
        sender.sendMessage(ChatColor.BLUE + "Bank: " + ChatColor.WHITE + bal);
        sender.sendMessage(membersString);
        sender.sendMessage(ChatColor.RED + StringUtils.repeat("=", ChatColor.stripColor(titleString).length()-3));
    }

}
