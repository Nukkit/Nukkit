package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockSnowLayer;

/**
 * author: MagicDroidX
 * Modificated by NycuRO on 5.08.2017
 * Nukkit Project
 *
 * Info:
 * These biomes are known for their inclusion of Snow and Ice. Crops will grow slowly here.
 *
 * Biomes:
 * Frozen River
 * Ice Plains
 * Ice Plains Spikes
 * Cold Beach
 * Cold Taiga
 * Cold Taiga (Mountainous)
 */

public abstract class SnowyBiome extends NormalBiome {

    public SnowyBiome() {
        this.setGroundCover(new Block[]{
                new BlockSnowLayer(),
                new BlockGrass(),
                new BlockDirt(),
                new BlockDirt(),
                new BlockDirt()
        });
    }
}
