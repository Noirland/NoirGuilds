package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GuildAdminChatCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildAdminChatCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.debug("Command /ga | " + Arrays.toString(args));
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Consoles cannot send messages in guild admin chat.");
            return true;
        }
        Player player = (Player) sender;
        GuildMember gPlayer = gHandler.getGuildMember(player.getName());
        if(gPlayer == null || gPlayer.getGuild() == null || gPlayer.getRank() == null) {
            plugin.sendMessage(sender, "You are not currently in a guild.");
            return true;
        }

        GuildRank rank = gPlayer.getRank();
        if(!gHandler.hasPerm(gPlayer, RankPerm.ADMINCHAT)) {
            plugin.sendMessage(sender, "You haven't got permission to use Guild Admin chat.");
            return true;
        }

        String prefix = ChatColor.GREEN + "[A]" + ChatColor.GRAY + " [" + rank.getColour() + rank.getName() + ChatColor.GRAY + "] "
                + ChatColor.RESET + player.getName() + ChatColor.GRAY + ":" + ChatColor.RESET;
        String msg = prefix + GuildsUtil.arrayToString(args, 0, args.length - 1, " ");
        msg = ChatColor.translateAlternateColorCodes("&".charAt(0), msg);
        if(!(msg.length() == prefix.length())) {

            for(GuildRank r : gPlayer.getGuild().getRanks()) {
                if(gHandler.hasPerm(r, RankPerm.ADMINCHAT)) {
                    gHandler.sendMessageToRank(gPlayer.getGuild(), r, msg);
                }

            }
        }

        return true;
    }

}

