package me.zephirenz.noirguilds;

import me.zephirenz.noirguilds.database.DatabaseManager;
import me.zephirenz.noirguilds.database.DatabaseManagerFactory;
import me.zephirenz.noirguilds.objects.Guild;
import me.zephirenz.noirguilds.objects.GuildBankInventory;
import nz.co.noirland.bankofnoir.BankOfNoir;
import nz.co.noirland.bankofnoir.EcoManager;
import nz.co.noirland.bankofnoir.MoneyDenomination;
import nz.co.noirland.zephcore.UpdateInventoryTask;
import nz.co.noirland.zephcore.Util;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class BankManager implements Listener {

    private EcoManager eco;
    private DatabaseManager dbManager;

    private final Map<Guild, GuildBankInventory> openBanks = new HashMap<Guild, GuildBankInventory>();

    private boolean enabled = true;

    public BankManager() {
        if(BankOfNoir.inst() == null) {
            enabled = false;
            return;
        }
        this.dbManager = DatabaseManagerFactory.getDatabaseManager();
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
        GuildBankInventory bank = null;
        for(Map.Entry<Guild, GuildBankInventory> entry : openBanks.entrySet()) {
            if(entry.getValue().getBank().equals(inv)) {
                bank = entry.getValue();
                break;
            }
        }
        return bank;
    }

    public void removeOpenBank(GuildBankInventory bank) {
        if(bank.getBank().getViewers().size() > 1) return;
        openBanks.remove(bank.getOwner());
    }

    public GuildBankInventory createBank(Guild guild) {
        Inventory bank = BankOfNoir.inst().getServer().createInventory(null, EcoManager.BANK_SIZE, "Bank: " + ChatColor.GOLD + guild.getName());

        Double remainder = setBankContents(bank, guild.getBalance());

        return new GuildBankInventory(guild, bank, remainder);
    }

    /**
     * @return Remainder + overflow after inventory is loaded
     */
    private Double setBankContents(Inventory bank, Double balance) {
        bank.clear();

        HashMap<Integer, ItemStack> leftover = bank.addItem(eco.balanceToItems(balance));

        double remainder = 0.0;
        if(!leftover.isEmpty()) {
            for(ItemStack item : leftover.values()) {
                if(item == null) continue;
                if(!eco.isDenomination(item.getType())) continue;
                MoneyDenomination denom = eco.getDenomination(item.getType());
                remainder += item.getAmount() * denom.getValue();
            }
        }
        remainder += eco.getRemainder(balance);
        return remainder;
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
    public void onMoveItem(InventoryMoveItemEvent event) {
        NoirGuilds.debug().debug("Moved!");
    }

}
