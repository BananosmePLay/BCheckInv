package org.bananos.bcheckinv;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.InventoryView;

import java.util.HashMap;
import java.util.Map;

public class PlayerInventoryTracker implements Listener {
    private final BCheckInv plugin;
    private final InventoryManager inventoryManager;
    private final ConfigManager configManager;
    private final Map<Player, Player> activeViews = new HashMap<>();

    public PlayerInventoryTracker(BCheckInv plugin, InventoryManager inventoryManager,
                                  ConfigManager configManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.configManager = configManager;
    }

    public void addViewer(Player viewer, Player target) {
        activeViews.put(viewer, target);
    }

    public void removeViewer(Player viewer) {
        activeViews.remove(viewer);
    }

    public boolean isViewing(Player player) {
        return activeViews.containsKey(player);
    }

    public Player getTarget(Player viewer) {
        return activeViews.get(viewer);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        updateViewers(event.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        updateViewers(event.getPlayer());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            removeViewer((Player) event.getPlayer());
        }
    }

    public void updateViewers(Player target) {
        activeViews.forEach((viewer, t) -> {
            if (t.equals(target) && viewer.isOnline()) {
                InventoryView view = viewer.getOpenInventory();
                if (view.getTopInventory().getSize() == 54 &&
                        view.getTitle().equals(configManager.getInventoryTitle(target))) {
                    inventoryManager.fillPlayerInventory(target, view.getTopInventory());
                    viewer.updateInventory();
                }
            }
        });
    }
}