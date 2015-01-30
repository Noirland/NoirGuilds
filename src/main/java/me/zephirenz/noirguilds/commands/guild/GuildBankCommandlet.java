package me.zephirenz.noirguilds.commands.guild;

import me.zephirenz.noirguilds.GuildBankManager;
import me.zephirenz.noirguilds.Perms;
import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class GuildBankCommandlet extends Commandlet {

    private final GuildBankManager bManager = plugin.getBankManager();

    /**
     *  The commandlet for viewing the guild's bank.
     *
     *  Usage: /guild bank (guild)
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(!checkPlayer(sender, NO_CONSOLE)) return;

        Guild guild;
        if(args.length > 0 && sender.hasPermission(Perms.BANK_OTHER)) {
            guild = gHandler.getGuildByName(args[0]);
            if(guild == null) {
                plugin.sendMessage(sender, GUILD_NOT_EXISTS);
                return;
            }
        } else {
            GuildMember member = gHandler.getMember((Player) sender);
            if(member == null) {
                plugin.sendMessage(sender, GUILD_BANK_NO_GUILD);
                return;
            }
            guild = member.getGuild();
        }
        Player p = (Player) sender;
        p.openInventory(bManager.getBank(guild).getBank());
    }

}
