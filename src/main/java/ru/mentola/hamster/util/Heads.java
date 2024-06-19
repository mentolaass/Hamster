package ru.mentola.hamster.util;

import org.bukkit.inventory.ItemStack;

public class Heads {
    public static ItemStack ZERO = Util.getHeadFromTexture("https://textures.minecraft.net/texture/1f886d9c40ef7f50c238824792c41fbfb54f665f159bf1bcb0b27b3ead373b");
    public static ItemStack ONE = Util.getHeadFromTexture("https://textures.minecraft.net/texture/a0a19e23d21f2db063cc55e99ae874dc8b23be779be34e52e7c7b9a25");
    public static ItemStack TWO = Util.getHeadFromTexture("https://textures.minecraft.net/texture/cc596a41daea51be2e9fec7de2d89068e2fa61c9d57a8bdde44b55937b6037");
    public static ItemStack THREE = Util.getHeadFromTexture("https://textures.minecraft.net/texture/b85d4fda56bfeb85124460ff72b251dca8d1deb6578070d612b2d3adbf5a8");
    public static ItemStack FOUR = Util.getHeadFromTexture("https://textures.minecraft.net/texture/3852a25fe69ca86fb982fb3cc7ac9793f7356b50b92cb0e193d6b4632a9bd629");
    public static ItemStack FIVE = Util.getHeadFromTexture("https://textures.minecraft.net/texture/74ee7d954eb14a5ccd346266231bf9a6716527b59bbcd7956cef04a9d9b");
    public static ItemStack SIX = Util.getHeadFromTexture("https://textures.minecraft.net/texture/2682a3ae948374e037e3d7dd687d59d185dd2cc8fc09dfeb42f98f8d259e5c3");
    public static ItemStack SEVEN = Util.getHeadFromTexture("https://textures.minecraft.net/texture/4ea30c24c60b3bc1af658ef661b771c48d5b9c9e28188cf9de9f832422e510");
    public static ItemStack EIGHT = Util.getHeadFromTexture("https://textures.minecraft.net/texture/66abafd023f230e4485aaf26e19368f5980d4f14a59fcc6d11a4466994892");
    public static ItemStack NINE = Util.getHeadFromTexture("https://textures.minecraft.net/texture/8d7910e10334f890a625483ac0c824b5e4a1a4b15a956327a3e3ae458d9ea4");

    public static ItemStack byInt(final int num) {
        return switch (num) {
            case 0 -> ZERO;
            case 1 -> ONE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> FOUR;
            case 5 -> FIVE;
            case 6 -> SIX;
            case 7 -> SEVEN;
            case 8 -> EIGHT;
            default -> NINE;
        };
    }
}
