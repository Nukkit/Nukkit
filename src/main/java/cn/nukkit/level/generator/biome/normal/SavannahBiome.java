package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.BlockSapling;
import cn.nukkit.level.generator.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorFlower;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.PopulatorTallGrass;
import cn.nukkit.level.generator.populator.tree.SavannaTreePopulator;

public class SavannahBiome extends GrassyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;

    public final int type;

    public SavannahBiome() {
        this(TYPE_NORMAL);
    }

    public SavannahBiome(int type) {
        super();
        this.type = type;
        this.temperature = 1.2f;
        this.rainfall = 0.0f;
        if (type == TYPE_NORMAL) {
            SavannaTreePopulator tree = new SavannaTreePopulator(BlockSapling.ACACIA);
            tree.setBaseAmount(1);
            PopulatorTallGrass tallGrass = new PopulatorTallGrass();
            tallGrass.setBaseAmount(20);
            PopulatorGrass grass = new PopulatorGrass();
            grass.setBaseAmount(20);
            PopulatorFlower flower = new PopulatorFlower();
            flower.setBaseAmount(4);
            this.addPopulator(tallGrass);
            this.addPopulator(grass);
            this.addPopulator(tree);
            this.addPopulator(flower);;
            this.setElevation(62, 68);
        } else {
            this.setElevation(62, 130);
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Savannah" : "Savannah M";
    }
}
