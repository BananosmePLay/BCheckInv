package org.bananos.bcheckinv;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    private final InventoryManager inventoryManager;
    private final ConfigManager configManager;
    private final PlayerInventoryTracker tracker;

    public InventoryListener(InventoryManager inventoryManager, ConfigManager configManager, PlayerInventoryTracker tracker) {
        this.inventoryManager = inventoryManager;
        this.configManager = configManager;
        this.tracker = tracker;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (!tracker.isViewing(player)) return;

        Player target = tracker.getTarget(player);

        // Проверка на кнопку закрытия
        if (event.getSlot() == configManager.getSlot("close_button")) {
            event.setCancelled(true);
            player.closeInventory();
            return;
        }

        // Проверка прав на изменение
        if (!player.hasPermission("bcheckinv.change")) {
            player.sendMessage(configManager.getMessage("no-permission-change"));
            event.setCancelled(true);
            return;
        }

        // Обновляем инвентарь целевого игрока
        updateTargetInventory(target, event.getSlot(), event.getCurrentItem());

        // Триггерим обновление
        tracker.updateViewers(target);
    }

    private void updateTargetInventory(Player target, int slot, ItemStack item) {
        if (slot == configManager.getSlot("helmet")) target.getInventory().setHelmet(item);
        else if (slot == configManager.getSlot("chestplate")) target.getInventory().setChestplate(item);
        else if (slot == configManager.getSlot("leggings")) target.getInventory().setLeggings(item);
        else if (slot == configManager.getSlot("boots")) target.getInventory().setBoots(item);
        else if (slot == configManager.getSlot("main_hand")) target.getInventory().setItemInMainHand(item);
        else if (slot == configManager.getSlot("off_hand")) target.getInventory().setItemInOffHand(item);
        else if (slot >= configManager.getSlot("hotbar_start") &&
                slot < configManager.getSlot("hotbar_start") + 9) {
            target.getInventory().setItem(slot - configManager.getSlot("hotbar_start"), item);
        }
        else if (slot >= configManager.getSlot("inventory_start") &&
                slot < configManager.getSlot("inventory_start") + 27) {
            target.getInventory().setItem(slot - configManager.getSlot("inventory_start") + 9, item);
        }
    }
}