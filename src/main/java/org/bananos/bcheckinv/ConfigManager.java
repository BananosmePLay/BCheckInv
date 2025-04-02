package org.bananos.bcheckinv;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        config.addDefault("messages.no-permission-use", "&cУ вас нет прав на использование этой команды!");
        config.addDefault("messages.no-permission-change", "&cУ вас нет прав на изменение инвентаря!");
        config.addDefault("messages.player-not-found", "&cИгрок не найден или не в сети!");
        config.addDefault("messages.usage", "&cИспользование: /invsee <ник игрока>");
        config.addDefault("settings.update-interval", 20);
        config.addDefault("panes.material", "ORANGE_STAINED_GLASS_PANE");
        config.addDefault("panes.name", " ");
        config.addDefault("buttons.close.material", "BARRIER");
        config.addDefault("buttons.close.name", "&cЗакрыть");
        config.addDefault("slots.helmet", 0);
        config.addDefault("slots.chestplate", 1);
        config.addDefault("slots.leggings", 2);
        config.addDefault("slots.boots", 3);
        config.addDefault("slots.main_hand", 5);
        config.addDefault("slots.off_hand", 6);
        config.addDefault("slots.close_button", 8);
        config.addDefault("slots.hotbar_start", 18);
        config.addDefault("slots.inventory_start", 27);

        config.options().copyDefaults(true);
        saveConfig();
    }

    public String getMessage(String path) {
        return colorize(config.getString("messages." + path, ""));
    }

    public int getUpdateInterval() {
        return config.getInt("settings.update-interval", 20);
    }

    public Material getPaneMaterial() {
        return Material.valueOf(config.getString("panes.material", "ORANGE_STAINED_GLASS_PANE"));
    }

    public String getPaneName() {
        return colorize(config.getString("panes.name", " "));
    }

    public Material getCloseButtonMaterial() {
        return Material.valueOf(config.getString("buttons.close.material", "BARRIER"));
    }

    public String getCloseButtonName() {
        return colorize(config.getString("buttons.close.name", "&cЗакрыть"));
    }

    public int getSlot(String type) {
        return config.getInt("slots." + type, -1);
    }

    public String getInventoryTitle(Player player) {
        return colorize("Инвентарь " + player.getName());
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Не удалось сохранить конфиг: " + e.getMessage());
        }
    }

    public String colorize(String message) {
        return message != null ? message.replace('&', '§') : "";
    }
}