package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildDisbandCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;

    public GuildDisbandCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
    }

    /**
     * The commandlet for disbanding a guild.
     * Usage: /guild disband (guild)
     *
     * @param sender the sender of the command
     * @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        String name;
        Guild guild;
        if(args.length == 1 && sender.hasPermission("noirguilds.disband.other")) {
            name = args[0];
            guild = gHandler.getGuild(name);
        }else{
            if(sender instanceof Player) {
                GuildMember gMember = gHandler.getGuildMember(sender.getName());
                if (gMember == null) {
                    plugin.sendMessage(sender, "You must be in a guild to leave it.");
                    return;
                }
                guild = gMember.getGuild();
                if(!gMember.getRank().isLeader()) {
                    plugin.sendMessage(sender, "You must be the leader to disband your guild.");
                    return;
                }
            }else{
                plugin.sendMessage(sender, "Console must specify a guild to disband.");
                return;
            }
        }

        gHandler.removeGuild(guild);

    }

}
