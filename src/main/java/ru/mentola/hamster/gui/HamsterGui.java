package ru.mentola.hamster.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.gui.api.InventoryGui;
import ru.mentola.hamster.model.HamsterUser;
import ru.mentola.hamster.util.Util;

@Getter
public final class HamsterGui extends InventoryGui {
    private final HamsterUser user;
    private int nextSlotClick;

    public HamsterGui(final Inventory inventory, final HamsterUser user) {
        super(inventory);

        this.user = user;
    }

    @Override
    public void open() {
        final Hamster hamster = Hamster.getInstance();
        if (hamster != null) hamster.getHamsterGuiPool().add(this);
        this.nextSlotClick = Util.updateHamsterGuiInventory(getInventory(), getUser());
    }

    @Override
    public void click(final int slot, final Player player) {
        if (slot != this.nextSlotClick) return;
        this.user.tap(player);
        this.nextSlotClick = Util.updateHamsterGuiInventory(getInventory(), this.user);
    }

    @Override
    public void close() {
        final Hamster hamster = Hamster.getInstance();
        if (hamster != null) hamster.getHamsterGuiPool().remove((gui) -> gui.equals(this));
    }
}
