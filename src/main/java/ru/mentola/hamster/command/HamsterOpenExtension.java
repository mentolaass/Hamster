package ru.mentola.hamster.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.command.api.ExtensionCommand;
import ru.mentola.hamster.gui.HamsterGui;
import ru.mentola.hamster.model.HamsterUser;
import ru.mentola.hamster.pool.HamsterUserPool;
import ru.mentola.hamster.util.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public final class HamsterOpenExtension implements ExtensionCommand {
    private final String name;

    @Override
    public void onCall(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Player player = (Player) commandSender;
        final Hamster plugin = Hamster.getInstance();

        if (plugin != null) {
            final HamsterUserPool userPool = plugin.getUserPool();
            final HamsterUser user = userPool.get((u) -> Util.getPlayerIntUniqueId(player) == u.getUniqueId());

            if (user == null) {
                player.sendMessage(plugin.getPluginConfig().getNoRegisteredAttemptOpenGuiMessage());
                return;
            }

            final Inventory inventory = Bukkit.createInventory(null, 54, Component.text(plugin.getPluginConfig().getHamsterGuiHeaderMessage()));
            final HamsterGui hamsterGui = new HamsterGui(inventory, user);
            hamsterGui.open();
            player.openInventory(hamsterGui.getInventory());
        }
    }

    @Override
    public List<String> onTab(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
