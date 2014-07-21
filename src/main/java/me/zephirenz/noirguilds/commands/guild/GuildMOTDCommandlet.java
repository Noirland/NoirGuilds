package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildMOTDCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildMOTDCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }


    /**
     *  The commandlet for editing guild motd.
     *  Usage: /guild motd [line] [value]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }

        if (args.length < 1) {
            plugin.sendMessage(sender, GUILD_MOTD_WRONG_ARGS);
            return;
        }
        GuildMember gMember = gHandler.getMember((Player) sender);
        if (gMember == null) {
            plugin.sendMessage(sender, GUILD_MOTD_NO_GUILD);
            return;
        }

        Guild guild = gMember.getGuild();
        if (!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, GUILD_MOTD_NOT_LEADER);
            return;
        }

        String sLine = args[0];
        int num;
        try {
            num = Integer.parseInt(sLine);
        } catch (NumberFormatException e) {
            plugin.sendMessage(sender, GUILD_MOTD_BAD_LINE);
            return;
        }
        String line = "";
        if(args.length > 1) {
            line = GuildsUtil.arrayToString(args, 1, args.length - 1, " ");
        }

        List<String> motd = guild.getMotd();
        motd.set(Math.min(motd.size(), num-1), line);
        guild.updateDB();
        plugin.sendMessage(sender, GUILD_MOTD_UPDATED);
    }

}
