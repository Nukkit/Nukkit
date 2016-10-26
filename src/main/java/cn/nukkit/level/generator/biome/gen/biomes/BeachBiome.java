package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSand;
import cn.nukkit.block.BlockSandstone;

/**
 * Author: PeratX, NycuRO
 * Nukkit Project
 */
public class BeachBiome extends SandyBiome {
    public BeachBiome() {
        //Todo: SugarCane

        this.setElevation(0.0f, 0.025f);
        this.temperature = 0.8f;
        this.rainfall = 0.4f;

        this.setGroundCover(new Block[]{
                new BlockSand(),
                new BlockSand(),
                new BlockSandstone(),
                new BlockSandstone(),
                new BlockSandstone()
        });
    }

    @Override
    public String getName() {
        return "Beach";
    }
    
    @Override
    public int getColor() {
        return 0xfade85;
    }
}
