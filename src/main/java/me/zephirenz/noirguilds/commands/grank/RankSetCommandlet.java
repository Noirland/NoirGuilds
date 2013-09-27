package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.GuildsHandler;
import me.zephirenz.noirguilds.NoirGuilds;
import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankSetCommandlet {

    NoirGuilds plugin;
    GuildsHandler gHandler;
    DatabaseManager dbManager;

    public RankSetCommandlet() {
        this.plugin = NoirGuilds.inst();
        this.gHandler = plugin.getGuildsHandler();
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
    }


    /**
     *  The commandlet for changing a member's rank.
     *  Usage: /guild set [player] [rank]
     *
     *  @param sender the sender of the command
     *  @param args   commandlet-specific args
     */
    public void run(CommandSender sender, String[] args) {

        if(!(sender instanceof Player)) {
            plugin.sendMessage(sender, "Console can not set member ranks.");
            return;
        }

        if(args.length != 2) {
            plugin.sendMessage(sender, "You must specify a member and rank to promote them to.");
            return;
        }

        String promote = args[0];
        String rankName = args[1];

        GuildMember mSender = gHandler.getGuildMember(sender.getName());
        GuildMember mPromote = gHandler.getGuildMember(promote);

        if(mSender == null) {
            plugin.sendMessage(sender, "You must be in a guild to do that.");
            return;
        }
        if(mPromote == null) {
            plugin.sendMessage(sender, "That player is not in a guild.");
            return;
        }

        if(!mSender.getRank().isLeader()) {
            plugin.sendMessage(sender, "You must be the leader to edit a member's rank.");
            return;
        }

        if(mSender.getGuild() != mPromote.getGuild()) {
            plugin.sendMessage(sender, "You must be in the same guild to edit a member's rank.");
            return;
        }

        if(mPromote.getRank().isLeader()) {
            plugin.sendMessage(sender, "You cannot change the leader's rank.");
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
            plugin.sendMessage(sender, "Rank does not exist!");
            return;
        }

        mPromote.setRank(newRank);
        dbManager.updateMemberRank(mPromote, newRank);
        plugin.sendMessage(sender, mPromote.getPlayer() + "'s rank was changed to " + newRank.getColour() + newRank.getName());

    }

}
