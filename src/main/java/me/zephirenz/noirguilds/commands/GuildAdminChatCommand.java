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

import static me.zephirenz.noirguilds.Strings.*;

public class GuildAdminChatCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildAdminChatCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return true;
        }
        Player player = (Player) sender;
        GuildMember member = gHandler.getMember(player);
        if(member == null) {
            plugin.sendMessage(sender, GUILD_CHAT_NO_GUILD);
            return false;
        }

        GuildRank rank = member.getRank();
        if(!member.hasPerm(RankPerm.ADMINCHAT)) {
            plugin.sendMessage(sender, GUILD_ACHAT_NO_PERMS);
            return true;
        }

        String prefix = String.format(GUILD_ACHAT_FORMAT, rank.getColour(), rank.getName(), player.getName());
        String msg = prefix + GuildsUtil.arrayToString(args, 0, args.length - 1, " ");
        msg = ChatColor.translateAlternateColorCodes("&".charAt(0), msg);
        if(!(msg.length() == prefix.length())) {

            for(GuildRank r : member.getGuild().getRanks()) {
                if(r.hasPerm(RankPerm.ADMINCHAT)) {
                    r.sendMessage(msg, false);
                }
            }
            return true;
        }
        return false;
    }

}

