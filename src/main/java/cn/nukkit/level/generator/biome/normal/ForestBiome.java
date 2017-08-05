package cn.nukkit.level.generator.biome.normal;

import a.e.P;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */

public class ForestBiome extends GrassyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_FLOWER = 1;
    public static final int TYPE_BIRCH = 2;
    public static final int TYPE_BIRCH_M = 3;
    public static final int TYPE_BIRCH_HILLS_M = 4;
    public static final int TYPE_ROOFED = 5;
    public static final int TYPE_ROOFED_M = 6;

    public final int type;

    public ForestBiome() {
        this(TYPE_NORMAL);
    }

    public ForestBiome(int type) {
        super();

        this.type = type;

        if (type == TYPE_NORMAL) {
            PopulatorTree trees = new PopulatorTree(BlockSapling.OAK);
            trees.setBaseAmount(5);
            this.addPopulator(trees);
            PopulatorGrass grass = new PopulatorGrass();
            grass.setBaseAmount(30);
            this.addPopulator(grass);
            PopulatorTallGrass tallGrass = new PopulatorTallGrass();
            tallGrass.setBaseAmount(3);
            this.addPopulator(tallGrass);

            this.setElevation(63, 68);
            this.temperature = 0.7f;
            this.rainfall = 0.8f;
        } else if (type == TYPE_FLOWER) {
            this.setElevation(63, 81);
            this.temperature = 0.7f;
            this.rainfall = 0.8f;
        } else if (type == TYPE_BIRCH) {
            this.setElevation(63, 81);
            this.temperature = 0.6f;
            this.rainfall = 0.6f;
        } else if (type == TYPE_BIRCH_M) {
            this.setElevation(63, 130);
            this.temperature = 0.6f;
            this.rainfall = 0.6f;
        } else if (type == TYPE_BIRCH_HILLS_M) {
            this.setElevation(63, 94);
            this.temperature = 0.6f;
            this.rainfall = 0.6f;
        } else if (type == TYPE_ROOFED) {
            this.setElevation(63, 81);
            this.temperature = 0.7f;
            this.rainfall = 0.8f;
        } else {
            this.setElevation(63, 130);
            this.temperature = 0.7f;
            this.rainfall = 0.8f;
        }
    }

    @Override
    public String getName() {
        String names = new String {
            "Forest",
            "Flower Forest",
            "Birch Forest",
            "Birch Forest M",
            "Birch Forest Hills M",
            "Roofed Forest",
            "Roofed Forest M"
        }
        return names;
    }
}
