package org.bananos.bcheckinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryManager {
    private final ConfigManager configManager;

    public InventoryManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void openInventory(Player viewer, Player target) {
        Inventory inv = Bukkit.createInventory(null, configManager.getSlot("size"),
                configManager.getInventoryTitle(target));

        setupDecoration(inv);
        fillPlayerInventory(target, inv);

        viewer.openInventory(inv);
    }

    private void setupDecoration(Inventory inv) {
        // Заполняем стеклянные панели
        ItemStack glass = configManager.getGlassPaneItem();
        for (int slot : configManager.getGlassPaneSlots()) {
            inv.setItem(slot, glass);
        }

        // Кнопка закрытия
        inv.setItem(configManager.getSlot("buttons.close"), configManager.getCloseButtonItem());
    }

    public void fillPlayerInventory(Player target, Inventory inv) {
        // Броня
        inv.setItem(0, target.getInventory().getHelmet());
        inv.setItem(1, target.getInventory().getChestplate());
        inv.setItem(2, target.getInventory().getLeggings());
        inv.setItem(3, target.getInventory().getBoots());


        // Руки
        inv.setItem(5, target.getInventory().getItemInMainHand());
        inv.setItem(6, target.getInventory().getItemInOffHand());

        // Хотбар
        for (int i = 0; i < 9; i++) {
            inv.setItem(18 + i, target.getInventory().getItem(i));
        }

        // Основной инвентарь
        for (int i = 0; i < 27; i++) {
            inv.setItem(27 + i, target.getInventory().getItem(9 + i));
        }
    }

    public void syncInventoryToPlayer(Player target, Inventory inv) {
        // Синхронизация из GUI в инвентарь игрока
        target.getInventory().setHelmet(inv.getItem(configManager.getSlot("armor.helmet")));
        target.getInventory().setChestplate(inv.getItem(configManager.getSlot("armor.chestplate")));
        target.getInventory().setLeggings(inv.getItem(configManager.getSlot("armor.leggings")));
        target.getInventory().setBoots(inv.getItem(configManager.getSlot("armor.boots")));

        target.getInventory().setItemInMainHand(inv.getItem(configManager.getSlot("hands.main")));
        target.getInventory().setItemInOffHand(inv.getItem(configManager.getSlot("hands.off")));

        // Копируем хотбар и основной инвентарь
        System.arraycopy(inv.getContents(), configManager.getSlot("hotbar-start"),
                target.getInventory().getContents(), 0, 9);
        System.arraycopy(inv.getContents(), configManager.getSlot("inventory-start"),
                target.getInventory().getContents(), 9, 27);
    }
}