package ru.mentola.hamster.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.manager.UserManager;
import ru.mentola.hamster.util.Util;

import static org.bukkit.Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP;

@Getter
public final class HamsterUser {
    private final int uniqueId;
    private final String username;
    private int totalCountCoins, totalCountTaps, level;
    @Setter
    private int referals;
    private long lastTimeStampClick;

    public HamsterUser(final int uniqueId, final String username, final int totalCountCoins, final int totalCountTaps, final int level, final int referals) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.totalCountTaps = totalCountTaps;
        this.totalCountCoins = totalCountCoins;
        this.level = level;
        this.referals = referals;
    }

    public void addCoins(final int value) {
        this.totalCountCoins += value;
    }

    public void tap(final Player player) {
        if (System.currentTimeMillis() - lastTimeStampClick >= 500) {
            this.level = Util.getLevelForTaps(this.totalCountTaps);
            this.totalCountCoins+=level+1;
            this.totalCountTaps++;

            if (this.totalCountTaps % (3 * level + 1) == 0) {
                final Hamster plugin = Hamster.getInstance();

                if (plugin != null) {
                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getUserManager().sync(this));
                }
            }

            lastTimeStampClick = System.currentTimeMillis();
            player.playSound(Sound.sound(BLOCK_BUBBLE_COLUMN_BUBBLE_POP.getKey(), Sound.Source.PLAYER, 100, 0));
        }
    }
}
