package me.zephirenz.noirguilds.objects;

import org.bukkit.inventory.Inventory;

public class GuildBankInventory {

    private final Guild owner;
    private final Inventory bank;
    private Double remainder;

    public GuildBankInventory(Guild owner, Inventory bank, Double remainder) {
        this.owner = owner;
        this.bank = bank;
        this.remainder = remainder;
    }

    public Guild getOwner() {
        return owner;
    }

    public Inventory getBank() {
        return bank;
    }

    public Double getRemainder() {
        return remainder;
    }

    public void setRemainder(Double remainder) {
        this.remainder = remainder;
    }

}
