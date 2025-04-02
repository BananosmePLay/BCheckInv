package org.bananos.bcheckinv;

import org.bukkit.plugin.java.JavaPlugin;

public class BCheckInv extends JavaPlugin {
    private InventoryManager inventoryManager;
    private ConfigManager configManager;
    private CommandHandler commandHandler;
    private PlayerInventoryTracker tracker;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.inventoryManager = new InventoryManager(this, configManager);
        this.tracker = new PlayerInventoryTracker(this, inventoryManager, configManager);
        this.commandHandler = new CommandHandler(this, inventoryManager, configManager, tracker);
        getCommand("invsee").setExecutor(commandHandler);
        getServer().getPluginManager().registerEvents(new InventoryListener(inventoryManager, configManager, tracker), this);
        getServer().getPluginManager().registerEvents(tracker, this);
        getLogger().info("BCheckInv with real-time updates enabled!");
    }

    @Override
    public void onDisable() {
        tracker.cleanup();
        getLogger().info("BCheckInv disabled!");
    }
}