package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.callbacks.InfoCallback;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildInfoCommandlet implements Commandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildInfoCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    /**
     *  The commandlet for showing guild info.
     *  Usage: /guild info (guild)
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        Guild guild;

        if(args.length >= 1) {
            String gName = args[0];

            guild = gHandler.getGuildByName(gName);
            if(guild == null) {
                guild = gHandler.getGuildByTag(gName);
            }

            if(guild == null) {
                plugin.sendMessage(sender, GUILD_NOT_EXISTS);
                return;
            }
        }else{
            if(!(sender instanceof Player)) {
                plugin.sendMessage(sender, NO_CONSOLE);
                return;
            }
            Player player = (Player) sender;
            GuildMember member = gHandler.getMember(player.getName());
            if(member == null) {
                plugin.sendMessage(sender, GUILD_LIST_NO_GUILD);
                return;
            }
            guild = member.getGuild();
        }

        new InfoCallback(sender, guild);
    }

}
