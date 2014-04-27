package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildTpCommand implements CommandExecutor {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildTpCommand() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length != 1) {
            plugin.sendMessage(sender, TP_NO_PLAYER);
            return true;
        }
        String tele = args[0];
        GuildMember mTele = gHandler.getGuildMember(tele);
        Player pTele = plugin.getServer().getPlayer(tele);
        if(pTele == null) {
            plugin.sendMessage(sender, PLAYER_NOT_ONLINE);
            return true;
        }

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return true;
        }

        GuildMember mSender = gHandler.getGuildMember(sender.getName());
        if (mSender == null) {
            plugin.sendMessage(sender, TP_NO_GUILD);
            return true;
        }

        if(mTele == null || !mTele.getGuild().equals(mSender.getGuild())) {
            plugin.sendMessage(sender, TP_NOT_IN_GUILD);
            return true;
        }

        if(!gHandler.hasPerm(mSender, RankPerm.TP)) {
            plugin.sendMessage(sender, TP_NO_PERMS);
            return true;
        }

        Player pSender = (Player) sender;
        Location loc = pTele.getLocation();

        pSender.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        plugin.sendMessage(pSender, String.format(Strings.TP_TELEPORTING, pTele.getName()));

        return true;
    }

}
