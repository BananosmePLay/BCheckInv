package org.bananos.bcheckinv;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("messages." + path));
    }

    public String getInventoryTitle(Player player) {
        String title = config.getString("inventory.title", "Инвентарь %player%");
        return ChatColor.translateAlternateColorCodes('&', title.replace("%player%", player.getName()));
    }

    public int getSlot(String path) {
        return config.getInt("inventory.slots." + path);
    }

    public List<Integer> getGlassPaneSlots() {
        return config.getIntegerList("inventory.decoration.glass.slots");
    }

    public ItemStack getGlassPaneItem() {
        return createItem("inventory.decoration.glass");
    }

    public ItemStack getCloseButtonItem() {
        return createItem("inventory.decoration.close-button");
    }

    private ItemStack createItem(String path) {
        Material material = Material.valueOf(config.getString(path + ".material"));
        String name = config.getString(path + ".name", "");

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);

        return item;
    }

    public int getInventorySize() {
        int size = config.getInt("inventory.size", 54);
        if (size <= 0 || size > 54) {
            plugin.getLogger().warning("Некорректный размер инвентаря! Используется значение по умолчанию 54");
            return 54;
        }
        return size;
    }

    public int getUpdateInterval() {
        return config.getInt("update-interval", 10);
    }

    public String getUsePermission() {
        return config.getString("permissions.use");
    }

    public String getChangePermission() {
        return config.getString("permissions.change");
    }
}