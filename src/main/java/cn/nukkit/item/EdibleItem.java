package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EdibleItem {
    public static int getRegainFoodLevel(int id) {
        return getRegainFoodLevel(id, 0);
    }

    public static int getRegainFoodLevel(int id, int meta) {
        switch (id) {
            case Item.APPLE:
                return 4;
            case Item.MUSHROOM_STEW:
                return 6;
            case Item.BEETROOT_SOUP:
                return 5;
            case Item.BREAD:
                return 5;
            case Item.RAW_PORKCHOP:
                return 3;
            case Item.COOKED_PORKCHOP:
                return 8;
            case Item.RAW_BEEF:
                return 3;
            case Item.STEAK:
                return 8;
            case Item.COOKED_CHICKEN:
                return 6;
            case Item.RAW_CHICKEN:
                return 2;
            case Item.MELON_SLICE:
                return 2;
            case Item.GOLDEN_APPLE:
                return 4;
            case Item.PUMPKIN_PIE:
                return 8;
            case Item.CARROT:
                return 3;
            case Item.POTATO:
                return 1;
            case Item.BAKED_POTATO:
                return 5;
            case Item.COOKIE:
                return 2;
            case Item.COOKED_FISH:
                switch (meta) {
                    case 1:
                        return 6;
                    default:
                        return 5;
                }
            case Item.RAW_FISH:
                switch (meta) {
                    case 1:
                        return 2;
                    case 2:
                        return 1;
                    case 3:
                        return 1;
                    default:
                        return 2;
                }
        }
        return 0;
    }

    public static double getRegainFoodSaturationLevel(int id) {
        return getRegainFoodSaturationLevel(id, 0);
    }

    public static double getRegainFoodSaturationLevel(int id, int meta) {
        switch (id) {
            case Item.APPLE:
                return 2.4;
            case Item.MUSHROOM_STEW:
                return 7.2;
            case Item.BEETROOT_SOUP:
                return 6.2;
            case Item.BREAD:
                return 6;
            case Item.RAW_PORKCHOP:
                return 1.8;
            case Item.COOKED_PORKCHOP:
                return 2.8;
            case Item.RAW_BEEF:
                return 1.8;
            case Item.STEAK:
                return 2.8;
            case Item.COOKED_CHICKEN:
                return 7.2;
            case Item.RAW_CHICKEN:
                return 1.2;
            case Item.MELON_SLICE:
                return 1.2;
            case Item.GOLDEN_APPLE:
                return 9.6;
            case Item.PUMPKIN_PIE:
                return 4.8;
            case Item.CARROT:
                return 4.8;
            case Item.POTATO:
                return 0.6;
            case Item.BAKED_POTATO:
                return 7.2;
            case Item.COOKIE:
                return 0.4;
            case Item.COOKED_FISH:
                switch (meta) {
                    case 1:
                        return 9.6;
                    default:
                        return 6;
                }
            case Item.RAW_FISH:
                switch (meta) {
                    case 1:
                        return 0.4;
                    case 2:
                        return 0.2;
                    case 3:
                        return 0.2;
                    default:
                        return 0.4;
                }
        }
        return 0;
    }
}
