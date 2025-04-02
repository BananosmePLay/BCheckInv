package org.bananos.bcheckinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
    private final BCheckInv plugin;
    private final ConfigManager configManager;

    public InventoryManager(BCheckInv plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void openInventory(Player viewer, Player target) {
        Inventory inv = Bukkit.createInventory(null, 54, configManager.getInventoryTitle(target));
        updateInventory(target, inv);
        viewer.openInventory(inv);
    }

    public void updateInventory(Player target, Inventory inv) {
        // Копируем весь инвентарь
        inv.setContents(target.getInventory().getContents());

        // Копируем броню и предметы в руках
        inv.setItem(36, target.getInventory().getHelmet());
        inv.setItem(37, target.getInventory().getChestplate());
        inv.setItem(38, target.getInventory().getLeggings());
        inv.setItem(39, target.getInventory().getBoots());
        inv.setItem(40, target.getInventory().getItemInOffHand());
    }
}