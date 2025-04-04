package org.bananos.bcheckinv;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryListener implements Listener {
    private final BCheckInv plugin;
    private final InventoryManager inventoryManager;
    private final ConfigManager configManager;
    private final PlayerInventoryTracker tracker;

    public InventoryListener(BCheckInv plugin, InventoryManager inventoryManager,
                             ConfigManager configManager, PlayerInventoryTracker tracker) {
        this.plugin = plugin;
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

        // Блокируем изменение без прав
        if (!viewer.hasPermission(configManager.getChangePermission())) {
            viewer.sendMessage(configManager.getMessage("no-permission-change"));
            event.setCancelled(true);
            return;
        }

        // Фикс дублирования - полная синхронизация
        if (event.getClickedInventory() != null &&
                event.getClickedInventory().getType() == InventoryType.PLAYER) {

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                inventoryManager.syncInventoryToPlayer(target, event.getView().getTopInventory());
                viewer.updateInventory();
            }, 1L);
        }
    }
}