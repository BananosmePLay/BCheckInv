package org.bananos.bcheckinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player viewer = (Player) event.getWhoClicked();
        if (!tracker.isViewing(viewer)) return;

        Player target = tracker.getTarget(viewer);
        if (target == null) return;

        // Блокируем изменение инвентаря без прав
        if (!viewer.hasPermission("bcheckinv.change")) {
            viewer.sendMessage(configManager.getMessage("no-permission-change"));
            event.setCancelled(true);
            return;
        }

        // Обновляем инвентарь в реальном времени
        Bukkit.getScheduler().runTaskLater(tracker.getPlugin(), () -> {
            inventoryManager.updateInventory(target, event.getInventory());
            target.updateInventory();
        }, 1L);
    }
}