package ru.mentola.hamster.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.command.api.ExtensionCommand;
import ru.mentola.hamster.model.HamsterUser;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor @Getter
public final class HamsterLeaderBordExtension implements ExtensionCommand {
    private final String name;

    @Override
    public void onCall(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final Hamster hamster = Hamster.getInstance();

        if (hamster != null) {
            if (strings.length != 2) {
                commandSender.sendMessage(hamster.getPluginConfig().getInvalidUsageLeaderBoardMessage());
                return;
            }
        }

        final String sortType = strings[1];

        if (hamster != null && (sortType.equalsIgnoreCase("taps")
                    || sortType.equalsIgnoreCase("coins")
                    || sortType.equalsIgnoreCase("level"))) {
            final List<HamsterUser> sortedUsers = hamster.getUserManager().getAll()
                    .stream()
                    .sorted((user, user_) -> sortType.equalsIgnoreCase("taps") ? Integer.compare(user_.getTotalCountTaps(), user.getTotalCountTaps())
                            : (sortType.equalsIgnoreCase("coins") ? Integer.compare(user_.getTotalCountCoins(), user.getTotalCountCoins()) : Integer.compare(user_.getLevel(), user.getLevel())))
                    .toList();

            final StringBuilder builder = getStringBuilder(sortType, sortedUsers, hamster);
            commandSender.sendMessage(builder.toString());
        } else if (hamster != null) {
            commandSender.sendMessage(hamster.getPluginConfig().getInvalidSortTypeLeaderBoardMessage());
        }
    }

    @NotNull
    private static StringBuilder getStringBuilder(final String sortType, final List<HamsterUser> sortedUsers, final Hamster plugin) {
        final StringBuilder builder = new StringBuilder();
        builder.append(plugin.getPluginConfig().getHeaderLeaderBoardMessage()
                .replaceAll("%parameter%", sortType));

        for (int i = 1; i < 12; i++) {
            try {
                final HamsterUser user = sortedUsers.get(i-1);
                final String username = user.getUsername();
                final int countData = sortType.equalsIgnoreCase("taps") ? user.getTotalCountTaps() : (sortType.equalsIgnoreCase("coins") ? user.getTotalCountCoins() : user.getLevel());
                builder.append(plugin.getPluginConfig().getRowLeaderBoardMessage()
                        .replaceAll("%leader_index%", String.valueOf(i))
                        .replaceAll("%leader_username%", username)
                        .replaceAll("%leader_data%", String.valueOf(countData)));
            } catch (Exception ignore) { }
        }

        builder.append("\n§7");
        return builder;
    }

    @Override
    public List<String> onTab(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Arrays.asList("taps", "coins", "level");
    }
}
