package ru.mentola.hamster.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.command.api.ExtensionCommand;
import ru.mentola.hamster.model.HamsterUser;
import ru.mentola.hamster.pool.HamsterUserPool;
import ru.mentola.hamster.util.Util;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public final class HamsterRegisterExtension implements ExtensionCommand {
    private final String name;

    @Override
    public void onCall(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Player player = (Player) commandSender;
        final Hamster plugin = Hamster.getInstance();

        if (plugin != null) {
            final HamsterUserPool userPool = plugin.getUserPool();

            if (userPool.get((user) -> Util.getPlayerIntUniqueId(player) == user.getUniqueId()) != null) {
                player.sendMessage(plugin.getPluginConfig().getAlreadyRegisteredMessage());
                return;
            }

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> registerPlayer(strings, plugin, player));
        }
    }

    private void registerPlayer(final String[] strings, final Hamster plugin, final Player player) {
        HamsterUser ambassador = getAmbassador(strings, plugin, player);
        int bonus = processAmbassador(ambassador);

        if (strings.length != 2 || ambassador != null) {
            HamsterUser user = createUser(player, bonus);
            plugin.getUserManager().add(user);
            plugin.getUserPool().add(user);

            if (ambassador != null) {
                syncAmbassadorData(ambassador, player, plugin);
            }

            player.sendMessage(plugin.getPluginConfig().getSuccessRegisterMessage());
        }
    }

    private int processAmbassador(final HamsterUser ambassador) {
        if (ambassador != null) {
            ambassador.setReferals(ambassador.getReferals() + 1);
            ambassador.addCoins(10000);
            return 10000;
        }
        return 0;
    }

    private HamsterUser createUser(final Player player, final int bonus) {
        return new HamsterUser(Util.getPlayerIntUniqueId(player), player.getName(), bonus, 0, 0, 0);
    }

    private void syncAmbassadorData(final HamsterUser ambassador, final Player player, final Hamster plugin) {
        Player ambassadorPlayer = Bukkit.getPlayer(ambassador.getUsername());
        plugin.getUserManager().sync(ambassador);

        plugin.getUserPool().getUnModifiableCache().forEach((u) -> {
            if (u.getUniqueId() == ambassador.getUniqueId()) {
                u.setReferals(ambassador.getReferals());
                u.addCoins(10000);
            }
        });

        if (ambassadorPlayer != null) {
            ambassadorPlayer.sendMessage(plugin.getPluginConfig().getAmbassadorSuccessMessage()
                    .replaceAll("%referal_name%", player.getName()));
        }
    }

    @Nullable
    private HamsterUser getAmbassador(final String[] args, final Hamster plugin, final Player player) {
        if (args.length == 2) {
            final String referal = args[1];
            if (!Util.isDigit(referal)) {
                player.sendMessage(plugin.getPluginConfig().getInvalidReferalCodeMessage());
            } else {
                final HamsterUser ambassador = plugin.getUserManager().getAll().stream()
                        .filter((user) -> user.getUniqueId() == Integer.parseInt(args[1]))
                        .findFirst()
                        .orElse(null);

                if (ambassador == null) {
                    player.sendMessage(plugin.getPluginConfig().getInvalidReferalCodeMessage());
                }

                return ambassador;
            }
        }
        return null;
    }

    @Override
    public List<String> onTab(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
