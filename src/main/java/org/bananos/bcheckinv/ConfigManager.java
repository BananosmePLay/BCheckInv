package org.bananos.bcheckinv;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        config.addDefault("messages.no-permission-use", "&cУ вас нет прав!");
        config.addDefault("messages.no-permission-change", "&cНельзя изменять!");
        config.addDefault("messages.player-not-found", "&cИгрок не найден!");
        config.addDefault("messages.usage", "&cИспользуйте: /invsee <ник>");
        config.options().copyDefaults(true);
        saveConfig();
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("messages." + path));
    }

    public String getInventoryTitle(Player player) {
        return ChatColor.RED + "Инвентарь: " + player.getName();
    }

    private void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().warning("Ошибка сохранения конфига");
        }
    }
}