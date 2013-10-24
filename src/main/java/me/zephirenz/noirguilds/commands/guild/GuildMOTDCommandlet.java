package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Util;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            plugin.sendMessage(sender, "Console cannot edit guild MOTD.");
            return;
        }

        if (args.length < 1) {
            plugin.sendMessage(sender, "You must specify a line and text to set the MOTD.");
            return;
        }
        GuildMember gMember = gHandler.getGuildMember(sender.getName());
        if (gMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to edit the MOTD.");
            return;
        }

        Guild guild = gMember.getGuild();
        if (!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, "You must be the leader of your guild to edit the MOTD.");
            return;
        }

        String sLine = args[0];
        int line;
        try {
            line = Integer.parseInt(sLine);
        } catch (NumberFormatException e) {
            plugin.sendMessage(sender, "Not a valid line number.");
            return;
        }
        String motd = "";
        if(args.length > 1) {
            motd = Util.arrayToString(args, 1, args.length - 1, " ");
        }

        dbManager.setMOTDLine(guild, line, motd);
        guild.setMotd(dbManager.getMOTD(guild));
        plugin.sendMessage(sender, "Updated guild MOTD.");
    }

}
