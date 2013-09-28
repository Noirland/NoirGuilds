package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class HQCommand implements CommandExecutor {

    NoirGuilds plugin;
    GuildsHandler gHandler;
    DatabaseManager dbManager;

    public HQCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not teleport to HQ's.");
            return true;
        }

        GuildMember gMember = gHandler.getGuildMember(sender.getName());
        if (gMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to use HQ's.");
            return true;
        }

        Guild guild = gMember.getGuild();

        if(args.length == 0) {
            if(!gHandler.hasPerm(gMember, RankPerm.HQ)) {
                plugin.sendMessage(sender, "You don't have permission to teleport.");
                return true;
            }
            teleport(guild, (Player) sender);
            return true;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("set")) {
            if(!gMember.getRank().isLeader()) {
                plugin.sendMessage(sender, "You must be the leader of your guild to create HQ's.");
                return true;
            }
            createHq(guild, (Player) sender);
            return true;
        }



        return true;

    }

    private void teleport(Guild guild, Player player) {
        Location loc = dbManager.getHq(guild);
        if(loc == null) {
            plugin.sendMessage(player, "No HQ set for guild.");
            return;
        }
        player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        plugin.sendMessage(player, ChatColor.GOLD + "Teleporting to HQ...");
    }

    private void createHq(Guild guild, Player player) {

        dbManager.setHq(guild, player.getLocation());
        plugin.sendMessage(player, "Your guild's HQ has been set here.");

    }


}
