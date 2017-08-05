package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.level.generator.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.PopulatorLilyPad;
import cn.nukkit.level.generator.populator.tree.SwampTreePopulator;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class SwamplandBiome extends GrassyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public SwamplandBiome() {
        this(TYPE_NORMAL);
    }

    public SwamplandBiome(int type) {
        super();
        this.type = type;
        this.temperature = 0.8f;
        this.rainfall = 0.9f;
        if (type == TYPE_NORMAL) {
            PopulatorLilyPad lilypad = new PopulatorLilyPad();
            lilypad.setBaseAmount(4);

            SwampTreePopulator trees = new SwampTreePopulator();
            trees.setBaseAmount(2);

            PopulatorFlower flower = new PopulatorFlower();
            flower.setBaseAmount(2);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_BLUE_ORCHID);

            this.addPopulator(trees);
            this.addPopulator(flower);
            this.addPopulator(lilypad);

            this.setElevation(62, 63);
        } else {
            this.setElevation(63, 74); // Need check
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Swampland" : "Swampland M";
    }

    @Override
    public int getColor() {
        return 0x6a7039;
    }

    @Override
    public float setWaterColor() {
        return 3764330;
    }
}
