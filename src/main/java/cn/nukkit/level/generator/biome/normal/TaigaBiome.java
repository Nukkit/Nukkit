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

        this.temperature = 0.05f;
        this.rainfall = 0.8f;

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

            this.setElevation(63, 81);
        } else if (type == TYPE_M) {
            this.setElevation(63, 130);
        } else if (type == TYPE_MEGA) {
            this.setElevation(63, 81);
        } else {
            this.setElevation(63, 81);
        }
    }

    @Override
    public String getName() {
        String names = new String {
                "Taiga",
                "Taiga M",
                "Mega Taiga",
                "Mega Spruce Taiga"
        }
        return names;
    }
}
