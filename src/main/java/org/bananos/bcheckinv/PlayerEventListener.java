public class PlayerEventListener implements Listener {
    private final InventoryManager inventoryManager;

    public PlayerEventListener(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    @EventHandler
    public void onInventoryChange(PlayerPickupItemEvent event) {
        updateViewers(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        updateViewers(event.getPlayer());
    }

    private void updateViewers(Player target) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            Inventory openInv = viewer.getOpenInventory();
            if (openInv.getTitle().equals("Инвентарь " + target.getName())) {
                inventoryManager.openInventoryMenu(viewer, target);
            }
        }
    }
}