package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.item.enchantment.protection.EnchantmentProtection;
import cn.nukkit.level.generator.biome.SnowyBiome;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX
 * Modificated by NycuRO on 05.08.2017
 * Nukkit Project
 *
 * These biomes are a part of SnowyBiomes.
 * Biomes: Ice Plains and Ice Plains Spikes.
 *
 * Info:
 * An expansive, flat biome with a huge amount of snow.
 * All sources of water exposed to the sky are frozen over.
 * Sugar cane will generate in this biome, but can become uprooted when chunks load as the water sources freeze to ice.
 * There are very few natural oak and spruce trees in this biome.
 * It has a lower chance of spawning passive mobs during world generation than other biomes (7% versus 10%), however it is one of the few biomes where Polar bears and Strays spawn.
 * Due to the biome's size, snow and ice cover, and scarcity of wood and animals, initial survival becomes difficult in comparison to other biomes.
 * This is one of only two biomes where igloos naturally generate.
 */

public class IcePlainsBiome extends SnowyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SPIKES = 1;

    public final int type;

    public IcePlainsBiome() {
        this(TYPE_NORMAL);
    }

    public IcePlainsBiome(int type) {
        super();
        this.type = type;
        this.temperature = 0.0f;
        this.rainfall = 0.5f;
        if (type == TYPE_NORMAL) {
            PopulatorTallGrass tallGrass = new PopulatorTallGrass();
            tallGrass.setBaseAmount(5);

            PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
            trees.setBaseAmount(1);
            trees.setRandomAmount(1);
            this.addPopulator(tallGrass);
            this.addPopulator(trees);
            this.setElevation(63, 74);
        } else {
            this.setElevation(63, 74);
        }
    }

    public String getName() {
        return this.type == TYPE_NORMAL ? "Ice Plains" : "Ice Plains Spikes";
    }
}
