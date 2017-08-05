package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SnowyBiome;

/**
 * Created by NycuRO on 05.08.2017
 *
 * These biomes is a part of SnowyBiome and MountainBiome.
 *
 * Info ColdTaiga:
 *
 * Much like the regular taiga, the cold taiga is a relatively flat biome with large expanses of spruce trees.
 * Ferns, and their taller variants, generate here quite commonly, although regular tall grass can still be found.
 * It is one of the few places where wolves will naturally spawn.
 * One may also find an igloo nestled between the trees, making it one of only two biomes where igloos naturally generate.
 *
 * Info ColdTaiga M:
 *
 * The cold taiga M is not nearly as flat as its regular counterpart.
 * Compared to regular taiga hills, the hills found in this biome are much steeper and more erratic.
 * This large height differences make navigating the cold taiga M biome quite dangerous.
 * Also unlike its normal variant, igloos do not generate here.
 */

public class ColdTaiga extends SnowyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public ColdTaiga() {
        this(TYPE_NORMAL);
    }

    public ColdTaiga(int type) {
        super();

        this.type = type;
        this.temperature = -0.5f;
        this.rainfall = 0.4f;
        if (type == TYPE_NORMAL) {
            this.setElevation(63, 74);
        } else {
            this.setElevation(63,127);
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Cold Taiga" : "Cold Taiga M";
    }
}
