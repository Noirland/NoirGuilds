package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildEditCommandlet extends Commandlet {

    /**
     *  The commandlet for editing guild options.
     *  Usage: /guild edit [name|tag] [value]
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(!checkPlayer(sender, NO_CONSOLE)) return;

        if(args.length != 2) {
            plugin.sendMessage(sender, GUILD_EDIT_WRONG_ARGS);
            return;
        }

        String option = args[0];
        String value = args[1];

        GuildMember gMember = gHandler.getMember((Player) sender);
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
            editTag(sender, guild, value);
        }
    }

    public void editName(CommandSender sender, Guild guild, String name) {
        for(Guild g : gHandler.getGuilds()) {
            if(g.getName().equalsIgnoreCase(name)) {
                plugin.sendMessage(sender, GUILD_EXISTS);
                return;
            }
        }
        guild.setName(name);
        guild.updateDB();
        plugin.sendMessage(sender, String.format(GUILD_EDIT_NAME_CHANGED, guild.getName()));
    }

    public void editTag(CommandSender sender, Guild guild, String tag) {

        if(tag.length() > 4) {
            plugin.sendMessage(sender, BIG_TAG);
            return;
        }

        for(Guild g : gHandler.getGuilds()) {
            if(g.getTag().equalsIgnoreCase(tag)) {
                plugin.sendMessage(sender, TAG_EXISTS);
                return;
            }
        }
        guild.setTag(tag);
        guild.updateDB();
        plugin.sendMessage(sender, String.format(GUILD_EDIT_TAG_CHANGED, guild.getTag()));

    }

}
