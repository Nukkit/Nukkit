package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemArrow extends Item {
    
    public static final int NO_EFFECT = 0;

    public static final int NIGHT_VISION = 6;
    public static final int NIGHT_VISION_LONG = 7;
    public static final int INVISIBLE = 8;
    public static final int INVISIBLE_LONG = 9;
    public static final int LEAPING = 10;
    public static final int LEAPING_LONG = 11;
    public static final int LEAPING_II = 12;
    public static final int FIRE_RESISTANCE = 13;
    public static final int FIRE_RESISTANCE_LONG = 14;
    public static final int SPEED = 15;
    public static final int SPEED_LONG = 16;
    public static final int SPEED_II = 17;
    public static final int SLOWNESS = 18;
    public static final int SLOWNESS_LONG = 19;
    public static final int WATER_BREATHING = 20;
    public static final int WATER_BREATHING_LONG = 21;
    public static final int INSTANT_HEALTH = 22;
    public static final int INSTANT_HEALTH_II = 23;
    public static final int HARMING = 24;
    public static final int HARMING_II = 25;
    public static final int POISON = 26;
    public static final int POISON_LONG = 27;
    public static final int POISON_II = 28;
    public static final int REGENERATION = 29;
    public static final int REGENERATION_LONG = 30;
    public static final int REGENERATION_II = 31;
    public static final int STRENGTH = 32;
    public static final int STRENGTH_LONG = 33;
    public static final int STRENGTH_II = 34;
    public static final int WEAKNESS = 35;
    public static final int WEAKNESS_LONG = 36;
    public static final int WITHER_II = 37;
   
    
    public ItemArrow() {
        this(0, 1);
    }

    public ItemArrow(Integer meta) {
        this(meta, 1);
    }

    public ItemArrow(Integer meta, int count) {
        super(ARROW, meta, count, "Arrow");
    }

}
