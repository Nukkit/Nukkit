package cn.nukkit.level.generator.biome.normal;

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
            this.setElevation(125, 130); // Need check
            this.temperature = 0.6f;
            this.rainfall = 0.6f;
        } else if (type == TYPE_BIRCH_HILLS_M) {
            this.setElevation(125, 130); // Need check
            this.temperature = 0.6f;
            this.rainfall = 0.6f;
        } else if (type == TYPE_ROOFED) {
            this.setElevation(63, 81);
            this.temperature = 0.7f;
            this.rainfall = 0.8f;
        } else {
            this.setElevation(125, 130); // Need check
            this.temperature = 0.7f;
            this.rainfall = 0.8f;
        }
    }

    @Override
    public String getName() {
        if (type == TYPE_NORMAL) {
            return "Forest";
        } else if (type == TYPE_FLOWER) {
            return "Sunflower Forest";
        } else if (type == TYPE_BIRCH) {
            return "Birch Forest";
        } else if (type == TYPE_BIRCH_M) {
            return "Birch Forest M";
        } else if (type == TYPE_BIRCH_HILLS_M) {
            return "Birch Forest Hills M";
        } else if (type == TYPE_ROOFED) {
            return "Roofed Forest";
        } else {
            return "Roofed Forest M";
        }
    }
}
