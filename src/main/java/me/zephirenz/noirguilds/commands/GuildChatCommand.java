package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.GUILD_CHAT_NO_GUILD;
import static me.zephirenz.noirguilds.Strings.NO_CONSOLE;

public class GuildChatCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildChatCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return true;
        }
        Player player = (Player) sender;
        GuildMember gPlayer = gHandler.getGuildMember(player.getUniqueId());
        if(gPlayer == null || gPlayer.getGuild() == null || gPlayer.getRank() == null) {
            plugin.sendMessage(sender, GUILD_CHAT_NO_GUILD);
            return false;
        }

        GuildRank rank = gPlayer.getRank();
        String prefix = ChatColor.RED + "[G]" + ChatColor.GRAY + " [" + rank.getColour() + rank.getName() + ChatColor.GRAY + "] "
                            + ChatColor.RESET + player.getName() + ChatColor.RED + ":" + ChatColor.RESET;
        String msg = prefix + GuildsUtil.arrayToString(args, 0, args.length - 1, " ");
        msg = ChatColor.translateAlternateColorCodes("&".charAt(0), msg);
        if(!(msg.length() == prefix.length())) {
            gPlayer.getGuild().sendMessage(msg);
            return true;
        }

        return false;
    }

}
