package me.zephirenz.noirguilds.commands.grank;

import me.zephirenz.noirguilds.commands.Commandlet;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.GuildMember;
import me.zephirenz.noirguilds.objects.GuildRank;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.zephirenz.noirguilds.Strings.*;

public class RankEditCommandlet extends Commandlet {

    /**
     *  The commandlet for editing a rank.
     *  Usage: /grank edit [rank] [option] [value]
     */
    @Override
    public void run(CommandSender sender, String[] args) {
        if(isNotPlayer(sender, NO_CONSOLE)) return;

        if(args.length != 3) {
            plugin.sendMessage(sender, RANK_EDIT_WRONG_ARGS);
            return;
        }
        String rankName = args[0];
        String option = args[1];
        String value = args[2];

        GuildMember mSender = gHandler.getMember((Player) sender);

        if(isNull(mSender, sender, RANK_EDIT_NO_GUILD)) return;
        if(!mSender.getRank().isLeader()) {
            plugin.sendMessage(sender, RANK_EDIT_NOT_LEADER);
            return;
        }

        GuildRank rank = null;
        for(GuildRank r : mSender.getGuild().getRanks()) {
            if(r.getName().equalsIgnoreCase(rankName)) {
                rank = r;
                break;
            }
        }
        if(isNull(rank, sender, RANK_NOT_EXISTS)) return;

        if(option.equalsIgnoreCase("colour")) {
            editColour(sender, rank, value);
        }else if(option.equalsIgnoreCase("name")) {
            editName(sender, rank, value);
        }else{
            editPerm(sender, rank, option, value);
        }

    }

    private void editPerm(CommandSender sender, GuildRank rank, String permName, String value) {
        RankPerm perm = RankPerm.get(permName);
        if(isNull(perm, sender, RANK_EDIT_BAD_OPTION)) return;

        boolean val = Boolean.valueOf(value);
        rank.setPerm(perm, val);
        rank.updateDB();
        if(!val) {
            plugin.sendMessage(sender, String.format(RANK_EDIT_NO_PERM, rank.getName(), perm.getPerm()));
        }else{
            plugin.sendMessage(sender, String.format(RANK_EDIT_PERM, rank.getName(), perm.getPerm()));
        }
    }

    private void editColour(CommandSender sender, GuildRank rank, String value) {
        ChatColor colour;
        try{
            colour = ChatColor.valueOf(value);
        }catch(IllegalArgumentException e) {
            plugin.sendMessage(sender, RANK_EDIT_BAD_COLOUR);
            return;
        }

        rank.setColour(colour);
        rank.updateDB();
        plugin.sendMessage(sender, String.format(RANK_EDIT_SET_COLOUR, colour + colour.name()));

    }
    private void editName(CommandSender sender, GuildRank rank, String value) {
        for(GuildRank r : rank.getGuild().getRanks()) {
            if(r.getName().equalsIgnoreCase(value)) {
                plugin.sendMessage(sender, RANK_EXISTS);
                return;
            }
        }

        rank.setName(value);
        rank.updateDB();
        plugin.sendMessage(sender, RANK_EDIT_SET_NAME);

    }

}
