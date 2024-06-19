package ru.mentola.hamster.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.gui.HamsterGui;
import ru.mentola.hamster.pool.HamsterGuiPool;
import ru.mentola.hamster.util.Util;

public final class InventoryListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(final InventoryClickEvent e) {
        final Hamster hamster = Hamster.getInstance();

        if (hamster != null
                && e.getWhoClicked() instanceof Player player) {
            final HamsterGuiPool hamsterGuiPool = hamster.getHamsterGuiPool();
            final HamsterGui hamsterGui = hamsterGuiPool.get((gui) -> gui.getUser().getUniqueId() == Util.getPlayerIntUniqueId(player));

            if (hamsterGui != null
                    && hamsterGui.getInventory().equals(e.getClickedInventory())) {
                try {
                    if (e.getClick() == ClickType.LEFT)
                        hamsterGui.click(e.getSlot(), player);
                } catch (Exception ignored) { }

                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryMove(final InventoryMoveItemEvent e) {
        final Hamster hamster = Hamster.getInstance();

        if (hamster != null) {
            final HamsterGuiPool hamsterGuiPool = hamster.getHamsterGuiPool();
            final HamsterGui hamsterGui = hamsterGuiPool.get((gui) -> gui.getInventory().equals(e.getDestination()));

            if (hamsterGui != null)
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryPickup(final InventoryPickupItemEvent e) {
        processInventoryActions(e.getInventory(), e);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(final InventoryDragEvent e) {
        processInventoryActions(e.getInventory(), e);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryInteract(final InventoryInteractEvent e) {
        processInventoryActions(e.getInventory(), e);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(final InventoryCloseEvent e) {
        final Hamster hamster = Hamster.getInstance();

        if (hamster != null && e.getPlayer() instanceof Player player) {
            final HamsterGuiPool hamsterGuiPool = hamster.getHamsterGuiPool();
            final HamsterGui hamsterGui = hamsterGuiPool.get((gui) -> gui.getUser().getUniqueId() == Util.getPlayerIntUniqueId(player));

            if (hamsterGui != null
                    && hamsterGui.getInventory().equals(e.getInventory())) {
                try {
                    hamsterGui.close();
                } catch (Exception ignored) { }
            }
        }
    }

    private void processInventoryActions(final Inventory inventory, final Cancellable e) {
        final Hamster hamster = Hamster.getInstance();

        if (hamster != null) {
            final HamsterGuiPool hamsterGuiPool = hamster.getHamsterGuiPool();
            final HamsterGui hamsterGui = hamsterGuiPool.get((gui) -> gui.getInventory().equals(inventory));

            if (hamsterGui != null)
                e.setCancelled(true);
        }
    }
}
