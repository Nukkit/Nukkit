package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSand;
import cn.nukkit.block.BlockSandstone;
import cn.nukkit.level.generator.biome.SandyBiome;
import cn.nukkit.level.generator.biome.SnowyBiome;

/**
 * Author: PeratX
 * Nukkit Project
 *
 */
public class BeachBiome extends SandyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_COLD = 1;
    public static final int TYPE_STONE = 2;

    public final int type;

    public BeachBiome() {
        this(TYPE_NORMAL);
    }

    public BeachBiome(int type) {
        super();
        this.type = type;

        if (type == TYPE_NORMAL) {
            // TODO: SugerCane Populator
            this.setElevation(62, 65);
            this.temperature = 0.8f;
            this.rainfall = 0.4f;

            this.setGroundCover(new Block[]{
                    new BlockSand(),
                    new BlockSand(),
                    new BlockSandstone(),
                    new BlockSandstone(),
                    new BlockSandstone()
            });
        } else if (type == TYPE_COLD) {
            this.temperature = 0.05f;
            this.rainfall = 0.3f;
            this.setElevation(62, 65);
        } else {
            this.temperature = 0.2f;
            this.rainfall = 0.3f;
            this.setElevation(62, 65);
        }
    }

    @Override
    public String getName() {
        if (type == TYPE_NORMAL) {
            return "Beach";
        } else if (type == TYPE_COLD) {
            return "Cold Beach";
        } else {
            return "Stone Beach";
        }
    }
}
