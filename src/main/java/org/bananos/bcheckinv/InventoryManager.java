package org.bananos.bcheckinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class InventoryManager {
    private final BCheckInv plugin;
    private final ConfigManager configManager;

    public InventoryManager(BCheckInv plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    public void openInventoryMenu(Player viewer, Player target) {
        Inventory inv = Bukkit.createInventory(null, 54, configManager.getInventoryTitle(target));

        // Заполняем стеклянные панели
        ItemStack pane = createGlassPane();
        for (int slot : Arrays.asList(4, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17)) {
            inv.setItem(slot, pane);
        }

        // Кнопка закрытия
        inv.setItem(configManager.getSlot("close_button"), createCloseButton());

        // Заполняем инвентарь
        updateInventorySilently(viewer, target, inv);

        viewer.openInventory(inv);
    }

    public void updateInventorySilently(Player viewer, Player target, Inventory inv) {
        // Броня и предметы в руках
        inv.setItem(configManager.getSlot("helmet"), target.getInventory().getHelmet());
        inv.setItem(configManager.getSlot("chestplate"), target.getInventory().getChestplate());
        inv.setItem(configManager.getSlot("leggings"), target.getInventory().getLeggings());
        inv.setItem(configManager.getSlot("boots"), target.getInventory().getBoots());
        inv.setItem(configManager.getSlot("main_hand"), target.getInventory().getItemInMainHand());
        inv.setItem(configManager.getSlot("off_hand"), target.getInventory().getItemInOffHand());

        // Хотбар
        int hotbarStart = configManager.getSlot("hotbar_start");
        for (int i = 0; i < 9; i++) {
            inv.setItem(hotbarStart + i, target.getInventory().getItem(i));
        }

        // Основной инвентарь
        int inventoryStart = configManager.getSlot("inventory_start");
        for (int i = 0; i < 27; i++) {
            inv.setItem(inventoryStart + i, target.getInventory().getItem(9 + i));
        }

        viewer.updateInventory();
    }

    private ItemStack createGlassPane() {
        ItemStack pane = new ItemStack(configManager.getPaneMaterial());
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(configManager.getPaneName());
        pane.setItemMeta(meta);
        return pane;
    }

    private ItemStack createCloseButton() {
        ItemStack button = new ItemStack(configManager.getCloseButtonMaterial());
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(configManager.getCloseButtonName());
        button.setItemMeta(meta);
        return button;
    }
}