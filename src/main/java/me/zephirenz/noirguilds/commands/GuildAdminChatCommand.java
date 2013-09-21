package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GuildAdminChatCommand implements CommandExecutor {

    NoirGuilds plugin;
    GuildsHandler gHandler;

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
        GuildMember gPlayer = gHandler.getGuildPlayer(player.getName());
        if(gPlayer == null || gPlayer.getGuild() == null || gPlayer.getRank() == null) {
            plugin.sendMessage(sender, "You are not currently in a guild.");
            return false;
        }

        GuildRank rank = gPlayer.getRank();
        if(gHandler.hasPerm(gPlayer, "adminchat")) {
            plugin.sendMessage(sender, "You haven't got permission to use Guild Admin chat.");
        }

        StringBuilder buffer = new StringBuilder();
        for (String arg : args) {
            buffer.append(' ').append(arg);
        }
        String prefix = ChatColor.GREEN + "[A]" + ChatColor.GRAY + " [" + rank.getColour() + rank.getName() + ChatColor.GRAY + "] "
                + ChatColor.RESET + player.getName() + ChatColor.GREEN + ": " + ChatColor.RESET;
        String msg = prefix + buffer.toString();
        if(!(buffer.toString().length() == 0)) {

            for(GuildRank r : gPlayer.getGuild().getRanks()) {
                if(r.getPerm("adminchat")) {
                    gHandler.sendMessageToRank(gPlayer.getGuild(), r, msg);
                }

            }
        }

        return false;
    }

}

