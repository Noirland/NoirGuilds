package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.databaseold.DatabaseManager;
import me.zephirenz.noirguilds.databaseold.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildMOTDCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final DatabaseManager dbManager;

    public GuildMOTDCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
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
        GuildMember gMember = gHandler.getGuildMember(sender.getName());
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
        int line;
        try {
            line = Integer.parseInt(sLine);
        } catch (NumberFormatException e) {
            plugin.sendMessage(sender, GUILD_MOTD_BAD_LINE);
            return;
        }
        String motd = "";
        if(args.length > 1) {
            motd = GuildsUtil.arrayToString(args, 1, args.length - 1, " ");
        }

        dbManager.setMOTDLine(guild, line, motd);
        guild.setMotd(dbManager.getMOTD(guild));
        plugin.sendMessage(sender, GUILD_MOTD_UPDATED);
    }

}
