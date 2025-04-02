package org.bananos.bcheckinv;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class BCheckInv extends JavaPlugin {
    private InventoryManager inventoryManager;
    private ConfigManager configManager;
    private PlayerInventoryTracker tracker;
    private Metrics metrics;

    @Override
    public void onEnable() {
        this.metrics = new Metrics(this, 25335);
        this.configManager = new ConfigManager(this);
        this.inventoryManager = new InventoryManager(this, configManager);
        this.tracker = new PlayerInventoryTracker(this, inventoryManager, configManager);
        getCommand("invsee").setExecutor(new CommandHandler(this, inventoryManager, configManager, tracker));
        getServer().getPluginManager().registerEvents(new InventoryListener(inventoryManager, configManager, tracker), this);
        getServer().getPluginManager().registerEvents(tracker, this);
        getLogger().info("BCheckInv enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BCheckInv disabled");
    }
}