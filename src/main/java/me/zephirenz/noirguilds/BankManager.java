package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.enums.RankPerm;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildBankInventory;
import me.zephirenz.noirguilds.objects.GuildMember;
import nz.co.noirland.bankofnoir.BankOfNoir;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.zephcore.UpdateInventoryTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class BankManager implements Listener {

    private EcoManager eco;
    private DatabaseManager dbManager;
    private GuildsHandler gHandler;

    private final Map<Guild, GuildBankInventory> openBanks = new HashMap<Guild, GuildBankInventory>();

    private boolean enabled = true;

    public BankManager() {
        if(BankOfNoir.inst() == null) {
            enabled = false;
            return;
        }
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
        this.gHandler = NoirGuilds.inst().getGuildsHandler();
        this.eco = BankOfNoir.getEco();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public GuildBankInventory getBank(Guild guild) {
        GuildBankInventory bank;
        if(openBanks.containsKey(guild)) {
            bank = openBanks.get(guild);
        } else {
            bank = createBank(guild);
            openBanks.put(guild, bank);
        }
        return bank;
    }

    public GuildBankInventory getOpenBank(Inventory inv) {
        for(GuildBankInventory entry : openBanks.values()) {
            if(entry.getBank().equals(inv)) {
                return entry;
            }
        }
        return null;
    }

    public void removeOpenBank(GuildBankInventory bank) {
        if(bank.getBank().getViewers().size() > 1) return;
        openBanks.remove(bank.getOwner());
    }

    public GuildBankInventory createBank(Guild guild) {
        Inventory bank = BankOfNoir.inst().getServer().createInventory(null, EcoManager.BANK_SIZE, "Bank: " + ChatColor.GOLD + guild.getName());

        Double remainder = eco.setBankContents(bank, guild.getBalance());

        return new GuildBankInventory(guild, bank, remainder);
    }

    public void updateBalance(Guild guild, double balance) {
        Double diff = balance - guild.getBalance();
        if(!openBanks.containsKey(guild)) return;
        GuildBankInventory bank = openBanks.get(guild);
        Double bankBal = eco.itemsToBalance(bank.getBank().getContents()) + bank.getRemainder();
        bank.setRemainder(eco.setBankContents(bank.getBank(), bankBal + diff));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCloseChest(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getPlayer();

        GuildBankInventory bank = getOpenBank(inv);
        if(bank == null) {
            return;
        }
        Guild owner = bank.getOwner();
        removeOpenBank(bank);

        double balance = owner.getBalance();
        double newBalance = eco.itemsToBalance(inv.getContents()) + bank.getRemainder();

        for(ItemStack item : inv.getContents()) {
            if(item == null) continue;
            if(eco.isDenomination(item.getType())) continue;

            PlayerInventory pInv = player.getInventory();
            if(pInv.firstEmpty() != -1) {
                pInv.addItem(item);
                new UpdateInventoryTask(player);
            }else{
                player.getWorld().dropItem(player.getLocation(), item);
            }
        }

        if(newBalance != balance) {
            dbManager.setBalance(owner, newBalance);
            owner.setBalance(newBalance);
            String action;

            if(newBalance > balance) {
                action = Strings.GUILD_BANK_DEPOSITED;
            }else {
                action = Strings.GUILD_BANK_WITHDREW;
            }
            BankOfNoir.sendMessage(player, String.format(action, eco.format(Math.abs(newBalance - balance))));

            OfflinePlayer pOwner = Util.player(owner.getLeader());
            if(!pOwner.equals(player) && pOwner.hasPlayedBefore() && pOwner.isOnline()) {
                BankOfNoir.sendMessage(pOwner.getPlayer(), String.format(action, eco.format(Math.abs(newBalance - balance))));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPickupItem(InventoryClickEvent event) {

        Inventory inv = event.getInventory();
        GuildBankInventory bank = getOpenBank(inv);
        if(inv.getType() != InventoryType.CHEST || bank == null) {
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
        GuildMember member = gHandler.getGuildMember(event.getWhoClicked().getName());
        if((member != null && member.getGuild() != null && member.getGuild().equals(bank.getOwner()))) {
            // If member is part of guild's bank, check if they have withdraw perms
            enabled = gHandler.hasPerm(member, RankPerm.BANK_WITHDRAW);
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
