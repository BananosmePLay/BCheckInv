package org.bananos.bcheckinv;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerInventoryTracker implements Listener {
    private final BCheckInv plugin;
    private final InventoryManager inventoryManager;
    private final ConfigManager configManager;
    private final Map<Player, Player> viewers = new HashMap<>();
    private final Map<Player, Inventory> inventories = new HashMap<>();

    public PlayerInventoryTracker(BCheckInv plugin, InventoryManager inventoryManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.configManager = configManager;
    }

    public void addViewer(Player viewer, Player target) {
        viewers.put(viewer, target);
        inventories.put(viewer, viewer.getOpenInventory().getTopInventory());
        startUpdateTask(viewer, target);
    }

    public void removeViewer(Player viewer) {
        viewers.remove(viewer);
        inventories.remove(viewer);
    }

    @EventHandler
    public void onInventoryChange(PlayerPickupItemEvent event) {
        updateViewers(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        updateViewers(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().startsWith("Инвентарь ")) {
            removeViewer((Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (viewers.containsKey((Player) event.getWhoClicked())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateViewers((Player) event.getWhoClicked());
                }
            }.runTaskLater(plugin, 1L);
        }
    }

    private void updateViewers(Player target) {
        viewers.forEach((viewer, trackedTarget) -> {
            if (trackedTarget.equals(target)) {
                inventoryManager.updateInventorySilently(viewer, target, inventories.get(viewer));
            }
        });
    }

    private void startUpdateTask(Player viewer, Player target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!viewers.containsKey(viewer)) {
                    cancel();
                    return;
                }
                inventoryManager.updateInventorySilently(viewer, target, inventories.get(viewer));
            }
        }.runTaskTimer(plugin, configManager.getUpdateInterval(), configManager.getUpdateInterval());
    }

    public boolean isViewing(Player player) {
        return viewers.containsKey(player);
    }

    public Player getTarget(Player viewer) {
        return viewers.get(viewer);
    }

    public void cleanup() {
        viewers.clear();
        inventories.clear();
    }
}