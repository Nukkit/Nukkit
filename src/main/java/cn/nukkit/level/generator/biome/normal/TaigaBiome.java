package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.biome.SnowyBiome;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.PopulatorTree;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class TaigaBiome extends SnowyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;
    public static final int TYPE_MEGA = 2;
    public static final int TYPE_SPRUCE = 3;

    public final int type;

    public TaigaBiome() {
        this(TYPE_NORMAL);
    }

    public TaigaBiome(int type) {
        super();
        this.type = type;

        if (type == TYPE_NORMAL) {
            PopulatorGrass grass = new PopulatorGrass();
            grass.setBaseAmount(6);
            this.addPopulator(grass);

            PopulatorTree trees = new PopulatorTree(BlockSapling.SPRUCE);
            trees.setBaseAmount(10);
            this.addPopulator(trees);

            PopulatorTallGrass tallGrass = new PopulatorTallGrass();
            tallGrass.setBaseAmount(1);

            this.addPopulator(tallGrass);

            this.temperature = 0.05f;
            this.rainfall = 0.8f;
            this.setElevation(63, 81);
        } else if (type == TYPE_M) {
            this.setElevation(63, 100);
            this.temperature = 0.05f;
            this.rainfall = 0.8f;
        } else if (type == TYPE_MEGA) {
            this.setElevation(63, 81);
            this.temperature = 0.3f;
            this.rainfall = 0.8f;
        } else {
            this.setElevation(63, 81);
            this.temperature = 0.25f;
            this.rainfall = 0.8f;
        }
    }

    @Override
    public String getName() {
        if (type == TYPE_NORMAL) {
            return "Taiga";
        } else if (type == TYPE_M) {
            return "Taiga M";
        } else if (type == TYPE_MEGA) {
            return "Mega Taiga";
        } else {
            return "Mega Spruce Taiga";
        }
    }
}
