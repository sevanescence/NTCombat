package com.makotomiyamoto.combat.entity;

import com.makotomiyamoto.combat.data.ItemType;
import org.bukkit.inventory.ItemStack;

public class ItemScannerThread extends Thread {
    ItemStack itemStack;
    ItemType itemType;
    public ItemScannerThread(ItemStack itemStack, ItemType itemType) {
        this.itemStack = itemStack;
        this.itemType = itemType;
    }
    @Override
    public void run() {

    }
}
