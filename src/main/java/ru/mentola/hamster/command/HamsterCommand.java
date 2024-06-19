package ru.mentola.hamster.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.command.api.ExtensionCommand;

import java.util.ArrayList;
import java.util.List;

public final class HamsterCommand implements CommandExecutor, TabExecutor {
    private final List<ExtensionCommand> extensions = new ArrayList<>();

    public HamsterCommand() {
        this.extensions.add(new HamsterRegisterExtension("register"));
        this.extensions.add(new HamsterOpenExtension("open"));
        this.extensions.add(new HamsterLeaderBordExtension("leaders"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length >= 1) {
                final ExtensionCommand extension = this.extensions.stream()
                        .filter((e) -> e.getName().equals(strings[0]))
                        .findFirst()
                        .orElse(null);

                if (extension == null) {
                    final Hamster hamster = Hamster.getInstance();
                    if (hamster != null) {
                        player.sendMessage(hamster.getPluginConfig().getNoFindExtensionMessage());
                    }
                    return true;
                }

                extension.onCall(commandSender, command, s, strings);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final List<String> response = new ArrayList<>();
        if (strings.length == 1) {
            this.extensions.forEach((e) -> response.add(e.getName()));
        } else {
            final ExtensionCommand extension = this.extensions.stream()
                    .filter((e) -> e.getName().equals(strings[0]))
                    .findFirst()
                    .orElse(null);
            if (extension != null)
                return extension.onTab(commandSender, command, s, strings);
        }
        return response;
    }
}
