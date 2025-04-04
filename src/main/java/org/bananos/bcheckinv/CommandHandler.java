package org.bananos.bcheckinv;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private final InventoryManager inventoryManager;
    private final ConfigManager configManager;
    private final PlayerInventoryTracker tracker;

    public CommandHandler(InventoryManager inventoryManager,
                          ConfigManager configManager,
                          PlayerInventoryTracker tracker) {
        this.inventoryManager = inventoryManager;
        this.configManager = configManager;
        this.tracker = tracker;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(configManager.getUsePermission())) {
            player.sendMessage(configManager.getMessage("no-permission-use"));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(configManager.getMessage("usage"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(configManager.getMessage("player-not-found"));
            return true;
        }

        inventoryManager.openInventory(player, target);
        tracker.addViewer(player, target);
        return true;
    }
}