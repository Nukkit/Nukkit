package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.level.generator.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.*;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PlainsBiome extends GrassyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SUNFLOWERS = 1;

    public final int type;

    public PlainsBiome() {
        this(TYPE_NORMAL);
    }

    public PlainsBiome(int type) {
        super();
        this.type = type;

        this.temperature = 0.8f;
        this.rainfall = 0.4f;
        this.setElevation(63, 68);
        if (type == TYPE_NORMAL) {
            PopulatorSugarcane sugarcane = new PopulatorSugarcane();
            sugarcane.setBaseAmount(6);
            PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
            tallSugarcane.setBaseAmount(60);
            PopulatorGrass grass = new PopulatorGrass();
            grass.setBaseAmount(40);
            PopulatorTallGrass tallGrass = new PopulatorTallGrass();
            tallGrass.setBaseAmount(7);
            PopulatorFlower flower = new PopulatorFlower();
            flower.setBaseAmount(10);
            flower.addType(Block.DANDELION, 0);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_POPPY);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_AZURE_BLUET);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_RED_TULIP);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_ORANGE_TULIP);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_WHITE_TULIP);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_PINK_TULIP);
            flower.addType(Block.RED_FLOWER, BlockFlower.TYPE_OXEYE_DAISY);

            this.addPopulator(sugarcane);
            this.addPopulator(tallSugarcane);
            this.addPopulator(grass);
            this.addPopulator(tallGrass);
            this.addPopulator(flower);
        } else {
            // Populators
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Plains" : "Sunflower Plains";
    }
}
