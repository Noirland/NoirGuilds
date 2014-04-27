package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.GuildsUtil;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

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
            plugin.sendMessage(sender, NO_CONSOLE);
            return;
        }

        if(args.length != 2) {
            plugin.sendMessage(sender, GUILD_EDIT_WRONG_ARGS);
            return;
        }

        String option = args[0];
        String value = args[1];

        GuildMember gMember = gHandler.getGuildMember(sender.getName());
        if (gMember == null) {
            plugin.sendMessage(sender, GUILD_EDIT_NO_GUILD);
            return;
        }

        Guild guild = gMember.getGuild();
        if(!gMember.getRank().isLeader()) {
            plugin.sendMessage(sender, GUILD_EDIT_NOT_LEADER);
            return;
        }

        if(option.equalsIgnoreCase("name")) {
            editName(sender, guild, value);
        } else if (option.equalsIgnoreCase("tag")) {
            //editTag(sender, guild, value);
            plugin.sendMessage(sender, GUILD_EDIT_TAGS_DISABLED);
        }
    }

    public void editName(CommandSender sender, Guild guild, String name) {
        for(Guild g : gHandler.getGuilds()) {
            if(g.getName().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, GUILD_EXISTS);
                return;
            }
        }
        dbManager.updateGuildName(guild, name);
        guild.setName(name);
        plugin.sendMessage(sender, String.format(GUILD_EDIT_NAME_CHANGED, guild.getName()));
    }

    public void editTag(CommandSender sender, Guild guild, String tag) {

        if(!GuildsUtil.isValidTag(tag)) {
            if(tag.length() <= 4) {
                plugin.sendMessage(sender, BIG_TAG);
            }
            plugin.sendMessage(sender, BAD_TAG_CHARS);
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getTag().equalsIgnoreCase(tag)) {
                plugin.sendMessage(sender, TAG_EXISTS);
                return;
            }
        }
        dbManager.updateGuildTag(guild, tag);
        guild.setTag(tag);
        plugin.sendMessage(sender, String.format(GUILD_EDIT_TAG_CHANGED, guild.getTag()));

    }

}
