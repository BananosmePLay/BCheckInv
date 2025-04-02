package org.bananos.bcheckinv;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerEventListener implements Listener {
    private final PlayerInventoryTracker tracker;

    public PlayerEventListener(PlayerInventoryTracker tracker) {
        this.tracker = tracker;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        tracker.updateViewers(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        tracker.updateViewers(event.getPlayer());
    }
}