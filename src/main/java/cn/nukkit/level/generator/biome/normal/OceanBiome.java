package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.Block;
import cn.nukkit.level.generator.biome.WateryBiome;
import cn.nukkit.level.generator.populator.PopulatorSugarcane;
import cn.nukkit.level.generator.populator.PopulatorTallSugarcane;

/**
 * author: MagicDroidX
 * Nukkit Project
 */

public class OceanBiome extends WateryBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_DEEP = 1;

    public final int type;

    public OceanBiome() {
        this(TYPE_NORMAL);
    }

    public OceanBiome(int type) {
        super();
        this.type = type;
        this.temperature = 0.5f;
        this.rainfall = 0.5f;
        if (type == TYPE_NORMAL) {
            PopulatorSugarcane sugarcane = new PopulatorSugarcane();
            sugarcane.setBaseAmount(6);
            PopulatorTallSugarcane tallSugarcane = new PopulatorTallSugarcane();
            tallSugarcane.setBaseAmount(60);
            this.addPopulator(sugarcane);
            this.addPopulator(tallSugarcane);
            this.setElevation(46, 58);
        } else {
            this.setElevation(30, 58); // Need check
        }
    }

    @Override
    public Block[] getGroundCover() {
        return super.getGroundCover();
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Ocean" : "Deep Ocean";
    }
}
