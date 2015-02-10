package me.zephirenz.noirguilds.commands;

import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import static me.zephirenz.noirguilds.Strings.*;

public class HQCommand extends Commandlet implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(isNotPlayer(sender, NO_CONSOLE)) return true;

        GuildMember gMember = gHandler.getMember(sender.getName());
        if(isNull(gMember, sender, HQ_NO_GUILD)) return true;

        Guild guild = gMember.getGuild();

        if(args.length == 0) {
            if(!gMember.hasPerm(RankPerm.HQ)) {
                plugin.sendMessage(sender, HQ_NO_PERMS);
                return true;
            }
            teleport(guild, (Player) sender);
            return true;
        }
        if(args.length == 1 && args[0].equalsIgnoreCase("set")) {
            if(!gMember.getRank().isLeader()) {
                plugin.sendMessage(sender, HQ_NOT_LEADER);
                return true;
            }
            setHQ(guild, ((Player) sender).getLocation());
            plugin.sendMessage(sender, HQ_SET);
            return true;
        }



        return true;

    }

    private void teleport(Guild guild, Player player) {
        Location loc = guild.getHQ();
        if(loc == null) {
            plugin.sendMessage(player, HQ_NO_HQ);
            return;
        }
        player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
        plugin.sendMessage(player, HQ_TELEPORTING);
    }

    private void setHQ(Guild guild, Location loc) {
        guild.setHQ(loc);
        guild.updateDB();
    }


    @Override
    public void run(CommandSender sender, String[] args) {
        onCommand(sender, null, "", args);
    }
}
