package ru.mentola.hamster.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.Nullable;
import ru.mentola.hamster.Hamster;
import ru.mentola.hamster.model.HamsterUser;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;

@UtilityClass
public class Util {
    private final Random random = new Random();
    private final Hamster hamster = Hamster.getInstance();

    public int getPlayerIntUniqueId(final Player player) {
        return player.getUniqueId().hashCode();
    }


    public boolean isDigit(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ignored) { }
        return false;
    }

    public int updateHamsterGuiInventory(final Inventory inventory, final HamsterUser user) {
        if (hamster == null)
            return Integer.MAX_VALUE;

        final int[] slots = new int[9];

        int index = 0;
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                slots[index++] = 30 + row * 9 + col;

        int greenWoolSlot = slots[random.nextInt(9)];

        for (final int slot : slots) {
            inventory.setItem(slot, slot == greenWoolSlot ? getGreenWool() : getRedWool());
        }

        inventory.setItem(37, getItemStatistic(user));
        inventory.setItem(43, getItemReferal(user));

        final int totalCoins = user.getTotalCountCoins();
        if (totalCoins == 0) {
            setDigits(inventory, 10, "0000000");
            return greenWoolSlot;
        }

        if (totalCoins >= 10000000) {
            setDigits(inventory, 10, "9999999");
            return greenWoolSlot;
        }

        setDigits(inventory, 10, formatCoins(totalCoins));
        return greenWoolSlot;
    }

    private void setDigits(final Inventory inventory, final int startIndex, final String digits) {
        for (int i = 0; i < digits.length(); i++) {
            inventory.setItem(startIndex + i, Heads.byInt(Character.getNumericValue(digits.charAt(i))));
        }
    }

    private String formatCoins(final int coins) {
        return String.format("%07d", coins);
    }

    private ItemStack getGreenWool() {
        final ItemStack itemStack = new ItemStack(Material.GREEN_WOOL, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(hamster.getPluginConfig().getClickWoolGreen()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getRedWool() {
        final ItemStack itemStack = new ItemStack(Material.RED_WOOL, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(hamster.getPluginConfig().getClickWoolRed()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getItemStatistic(final HamsterUser user) {
        final ItemStack itemStack = new ItemStack(Material.COMMAND_BLOCK, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(hamster.getPluginConfig().getStatHeader()));
        final List<String> lore = new ArrayList<>();
        lore.add(hamster.getPluginConfig().getStatTaps().replaceAll("%taps_count%", String.valueOf(user.getTotalCountTaps())));
        lore.add(hamster.getPluginConfig().getStatCoins().replaceAll("%coins_count%", String.valueOf(user.getTotalCountCoins())));
        lore.add(hamster.getPluginConfig().getStatReferals().replaceAll("%referals_count%", String.valueOf(user.getReferals())));
        lore.add(hamster.getPluginConfig().getStatLevel().replaceAll("%level%", String.valueOf(user.getLevel())));
        lore.add(hamster.getPluginConfig().getStatNeedToNextLevel().replaceAll("%next_level%", String.valueOf(user.getLevel()+1)).replaceAll("%next_level_taps_need%", String.valueOf(tapsToNextLevel(user.getTotalCountTaps()))));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getItemReferal(final HamsterUser user) {
        final ItemStack itemStack = new ItemStack(Material.DIAMOND, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(hamster.getPluginConfig().getReferalCode().replaceAll("%referal_code%", String.valueOf(user.getUniqueId()))));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Nullable
    public ItemStack getHeadFromTexture(final String texture) {
        try {
            final PlayerProfile player = Bukkit.createProfile(UUID.randomUUID());
            final PlayerTextures textures = player.getTextures();
            textures.setSkin(new URL(texture));
            player.setTextures(textures);
            final ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            final SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.displayName(Component.text(""));
            meta.setPlayerProfile(player);
            item.setItemMeta(meta);
            return item;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private int tapsForLevel(final int level) {
        if (hamster != null) {
            if (level <= 1) {
                return 0;
            }
            final double factor = (double) hamster.getPluginConfig().getMaxTaps() / (hamster.getPluginConfig().getMaxLevel() * hamster.getPluginConfig().getMaxLevel());
            return (int) (factor * (level - 1) * (level - 1));
        }
        return Integer.MAX_VALUE;
    }

    public int getLevelForTaps(final int taps) {
        if (hamster != null) {
            int level = 1;
            while (level <= hamster.getPluginConfig().getMaxLevel() && taps >= tapsForLevel(level + 1)) {
                level++;
            }
            return level;
        }
        return Integer.MAX_VALUE;
    }

    public int tapsToNextLevel(final int taps) {
        if (hamster != null) {
            final int currentLevel = getLevelForTaps(taps);
            if (currentLevel >= hamster.getPluginConfig().getMaxLevel()) {
                return Integer.MAX_VALUE;
            }
            int nextLevelExperience = tapsForLevel(currentLevel + 1);
            return nextLevelExperience - taps;
        }
        return Integer.MAX_VALUE;
    }
}
