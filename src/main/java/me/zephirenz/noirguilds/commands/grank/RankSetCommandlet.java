package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.Strings;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class RankSetCommandlet {

    private final NoirGuilds plugin;
    private final GuildsHandler gHandler;
    private final DatabaseManager dbManager;

    public RankSetCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
    }


    /**
     *  The commandlet for changing a member's rank.
     *  Usage: /grank set [player] [rank]
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
            plugin.sendMessage(sender, RANK_SET_WRONG_ARGS);
            return;
        }

        String promote = args[0];
        String rankName = args[1];

        GuildMember mSender = gHandler.getGuildMember(sender.getName());
        GuildMember mPromote = gHandler.getGuildMember(promote);

        if(mSender == null) {
            plugin.sendMessage(sender, RANK_SET_NO_GUILD);
            return;
        }
        if(mPromote == null) {
            plugin.sendMessage(sender, RANK_SET_TARGET_NO_GUILD);
            return;
        }

        if(!mSender.getRank().isLeader()) {
            plugin.sendMessage(sender, RANK_SET_NOT_LEADER);
            return;
        }

        if(mSender.getGuild() != mPromote.getGuild()) {
            plugin.sendMessage(sender, RANK_SET_NOT_SAME);
            return;
        }

        if(mPromote.getRank().isLeader()) {
            plugin.sendMessage(sender, RANK_SET_RANK_IS_LEADER);
            return;
        }

        Guild guild = mSender.getGuild();
        GuildRank newRank = null;

        for(GuildRank rank : guild.getRanks()) {
            if(rank.getName().equalsIgnoreCase(rankName)) {
                newRank = rank;
            }
        }
        if(newRank == null) {
            plugin.sendMessage(sender, RANK_NOT_EXISTS);
            return;
        }

        mPromote.setRank(newRank);
        dbManager.updateMemberRank(mPromote, newRank);
        plugin.sendMessage(sender, String.format(Strings.RANK_SET_CHANGED, mPromote.getPlayer(), newRank.getColour() + newRank.getName()));

    }

}
