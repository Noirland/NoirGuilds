package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Util;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildEditCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final DatabaseManager dbManager;

    public GuildEditCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
    }


    /**
     *  The commandlet for editing guild options.
     *  Usage: /guild edit [name|tag] [value]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console cannot edit guilds.");
            return;
        }

        if(args.length != 2) {
            plugin.sendMessage(sender, "You must specify an option and a value.");
            return;
        }

        String option = args[0];
        String value = args[1];

        GuildMember gMember = gHandler.getGuildMember(sender.getName());
        if (gMember == null) {
            plugin.sendMessage(sender, "You must be in a guild to edit it.");
            return;
        }

        Guild guild = gMember.getGuild();
        if(!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, "You must be the leader of your guild to edit it.");
            return;
        }

        if(option.equalsIgnoreCase("name")) {
            editName(sender, guild, value);
        } else if (option.equalsIgnoreCase("tag")) {
            //editTag(sender, guild, value);
            plugin.sendMessage(sender, "Changing tags is currently disbled. (Sorry!)");

        }



    }

    public void editName(CommandSender sender, Guild guild, String name) {
        for(Guild g : gHandler.getGuilds()) {
            if(g.getName().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, "A guild with that name already exists.");
                return;
            }
        }
        dbManager.updateGuildName(guild, name);
        guild.setName(name);
        plugin.sendMessage(sender, "Guild name changed to " + ChatColor.BLUE + name);
    }

    public void editTag(CommandSender sender, Guild guild, String tag) {

        if(!Util.isValidTag(tag)) {
            if(tag.length() <= 4) {
                plugin.sendMessage(sender, "Tags must be a maximum of 4 characters.");
            }
            plugin.sendMessage(sender, "Tags must only contain letters, numbers, periods, dashes, and underscores.");
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getTag().equalsIgnoreCase(tag)) {
                plugin.sendMessage(sender, "A guild with that tag already exists.");
                return;
            }
        }
        dbManager.updateGuildTag(guild, tag);
        guild.setTag(tag);
        plugin.sendMessage(sender, "Guild tag changed to " + ChatColor.GRAY + "[" + tag + "]");

    }

}
