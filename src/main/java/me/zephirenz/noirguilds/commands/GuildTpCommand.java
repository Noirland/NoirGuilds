package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.zephcore.Util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildTpCommand extends Commandlet implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!checkPlayer(sender, NO_CONSOLE)) return true;

        if(args.length != 1) {
            plugin.sendMessage(sender, TP_NO_PLAYER);
            return true;
        }

        String tele = args[0];
        GuildMember mTele = gHandler.getMember(tele);
        Player pTele = Util.player(tele).getPlayer();
        if(isNull(pTele, sender, PLAYER_NOT_ONLINE)) return true;

        GuildMember mSender = gHandler.getMember(sender.getName());
        if(isNull(mSender, sender, TP_NO_GUILD)) return true;

        if(mTele == null || !mTele.getGuild().equals(mSender.getGuild())) {
            plugin.sendMessage(sender, TP_NOT_IN_GUILD);
            return true;
        }

        if(!mSender.hasPerm(RankPerm.TP)) {
            plugin.sendMessage(sender, TP_NO_PERMS);
            return true;
        }

        Player pSender = (Player) sender;
        Location loc = pTele.getLocation();

        pSender.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        plugin.sendMessage(pSender, String.format(Strings.TP_TELEPORTING, pTele.getName()));

        return true;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        onCommand(sender, null, "", args);
    }
}
