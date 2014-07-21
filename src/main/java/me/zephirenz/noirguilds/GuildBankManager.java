package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.AbstractBankManager;
import nz.co.noirland.bankofnoir.BankInventory;
import nz.co.noirland.bankofnoir.BankOfNoir;
import nz.co.noirland.zephcore.UpdateInventoryTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GuildBankManager extends AbstractBankManager<Guild> {

    private GuildsHandler gHandler;

    private boolean enabled = true;

    public GuildBankManager() {
        if(BankOfNoir.inst() == null) {
            enabled = false;
            return;
        }
        this.gHandler = NoirGuilds.inst().getGuildsHandler();
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    protected double getBalance(Guild owner) {
        return owner.getBalance();
    }

    @Override
    protected String getName(Guild owner) {
        return owner.getName();
    }

    @Override
    public void updateBalance(Guild owner, Player updater, double balance, double diff) {
        owner.setBalance(balance);
        owner.updateDB();

        String action;

        if(diff > 0) {
            action = Strings.GUILD_BANK_DEPOSITED;
        }else {
            action = Strings.GUILD_BANK_WITHDREW;
        }
        BankOfNoir.sendMessage(updater, String.format(action, eco.format(Math.abs(diff))));

        for(GuildMember leader : owner.getMembersByRank(owner.getLeaderRank())) {
            OfflinePlayer pLeader = Util.player(leader.getPlayer());
            if(!pLeader.equals(updater) && pLeader.isOnline()) {
                BankOfNoir.sendMessage(pLeader.getPlayer(), String.format(action, eco.format(Math.abs(diff))));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPickupItem(InventoryClickEvent event) {

        Inventory inv = event.getInventory();
        BankInventory<Guild> bank = getOpenBank(inv);
        if(bank == null) {
            return;
        }

        boolean topClicked = event.getView().convertSlot(event.getRawSlot()) == event.getRawSlot();

        switch(event.getAction()) {
            case PICKUP_ALL:
            case PICKUP_SOME:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case SWAP_WITH_CURSOR:
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:
            case HOTBAR_MOVE_AND_READD:
            case HOTBAR_SWAP:
            case MOVE_TO_OTHER_INVENTORY:
            case CLONE_STACK:
                if(!topClicked) return; // Doesn't take things from bank
                break;

            case COLLECT_TO_CURSOR:
            case UNKNOWN:
                break;

            case NOTHING:
            case DROP_ALL_CURSOR:
            case DROP_ONE_CURSOR:
            case PLACE_ALL:
            case PLACE_SOME:
            case PLACE_ONE:
                return; //Ignore these, as they won't take things from the bank
        }

        boolean enabled;
        GuildMember member = gHandler.getMember((Player) event.getWhoClicked());
        if((member != null && member.getGuild() != null && member.getGuild().equals(bank.getOwner()))) {
            // If member is part of guild's bank, check if they have withdraw perms
            enabled = member.hasPerm(RankPerm.BANK_WITHDRAW);
        }else{
            // Otherwise, check if they have override perms
            enabled = event.getWhoClicked().hasPermission(Perms.BANK_OTHER);
        }

        if(enabled) return;

        // Player doesn't have permission to move item, cancel it.
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        new UpdateInventoryTask(player);
    }
}
