package org.bananos.bcheckinv;

import org.bukkit.plugin.java.JavaPlugin;

public class BCheckInv extends JavaPlugin {
    private InventoryManager inventoryManager;
    private ConfigManager configManager;
    private PlayerInventoryTracker tracker;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.inventoryManager = new InventoryManager(this, configManager);
        this.tracker = new PlayerInventoryTracker(this, inventoryManager, configManager);
        getCommand("invsee").setExecutor(new CommandHandler(this, inventoryManager, configManager, tracker));
        getServer().getPluginManager().registerEvents(new InventoryListener(inventoryManager, configManager, tracker), this);
        getServer().getPluginManager().registerEvents(tracker, this);
        getLogger().info("BCheckInv успешно запущен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BCheckInv отключен");
    }
}