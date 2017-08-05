package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.GrassyBiome;
import cn.nukkit.level.generator.populator.PopulatorGrass;
import cn.nukkit.level.generator.populator.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.tree.JungleTreePopulator;

public class JungleBiome extends GrassyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_M = 1;
    public static final int TYPE_EDGE = 2;
    public static final int TYPE_EDGE_M = 3;

    public final int type;

    public JungleBiome() {
        this(TYPE_NORMAL);
    }

    public JungleBiome(int type) {
        super();
        this.type = type;
        if (type == TYPE_NORMAL) {
            JungleTreePopulator trees = new JungleTreePopulator();
            JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
            trees.setBaseAmount(10);
            bigTrees.setBaseAmount(6);
            //PopulatorTallGrass tallGrass = new PopulatorTallGrass();

            PopulatorGrass grass = new PopulatorGrass();
            grass.setBaseAmount(20);

            //PopulatorFern fern = new PopulatorFern();
            //fern.setBaseAmount(30);

            this.addPopulator(grass);
            //this.addPopulator(fern);
            this.addPopulator(bigTrees);
            this.addPopulator(trees);
            this.setElevation(62, 63);
            this.temperature = 1.2f;
            this.rainfall = 0.9f;
        } else if (type == TYPE_M) {
            this.setElevation(62, 130);
            this.temperature = 1.2f;
            this.rainfall = 0.9f;
        } else if (type == TYPE_EDGE) {
            this.setElevation(62, 94);
            this.temperature = 0.95f;
            this.rainfall = 0.8f;
        } else {
            this.setElevation(62, 130);
            this.temperature = 0.95f;
            this.rainfall = 0.8f;
        }
    }

    @Override
    public String getName() {
        String names = new String {
            "Jungle",
            "Jungle M",
            "Jungle Edge",
            "Jungle Edge M"
        }
        return names;
    }
}
