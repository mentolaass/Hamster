package ru.mentola.hamster.gui.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@AllArgsConstructor @Getter
public abstract class InventoryGui {
    @Setter(AccessLevel.PROTECTED)
    private Inventory inventory;
    public abstract void open();
    public abstract void click(final int slot, final Player player);
    public abstract void close();
}
