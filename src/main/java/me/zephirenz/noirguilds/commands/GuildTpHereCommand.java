package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GuildTpHereCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildTpHereCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length != 1) {
            plugin.sendMessage(sender, "Must specify a player to teleport.");
            return true;
        }
        String tele = args[0];
        GuildMember mTele = gHandler.getGuildMember(tele);
        Player pTele = plugin.getServer().getPlayer(tele);
        if(pTele == null) {
            plugin.sendMessage(sender, "Player does not exist or is not online.");
            return true;
        }

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not teleport guild members.");
            return true;
        }

        GuildMember mSender = gHandler.getGuildMember(sender.getName());
        if (mSender == null) {
            plugin.sendMessage(sender, "You must be in a guild to teleport.");
            return true;
        }

        if(mTele == null || !mTele.getGuild().equals(mSender.getGuild())) {
            plugin.sendMessage(sender, "Player is not in your guild.");
            return true;
        }

        if(!gHandler.hasPerm(mSender, RankPerm.TPHERE)) {
            plugin.sendMessage(sender, "You don't have permission to teleport.");
            return true;
        }

        Player pSender = (Player) sender;
        Location loc = pSender.getLocation();

        pTele.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        plugin.sendMessage(pSender, ChatColor.GOLD + "Teleporting " + pTele.getName() + " to you...");

        return true;
    }

}
