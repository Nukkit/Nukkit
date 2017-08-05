package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.level.generator.biome.SandyBiome;

public class MesaBiome extends SandyBiome {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_BRYCE = 1;
    public static final int TYPE_PLATEAU = 2;
    public static final int TYPE_PLATEAU_M = 3;

    public final int type;

    public MesaBiome(int type) {
        super();
        this.type = type;
        this.temperature = 2.0f;
        this.rainfall = 0.0f;
        if (type == TYPE_NORMAL) {
            this.setElevation(63, 74);
        } else if (type == TYPE_BRYCE) {
            this.setElevation(64, 93);
        } else if (type == TYPE_PLATEAU) {
            this.setElevation(63, 74);
        } else {
            this.setElevation(63, 130);
        }
    }

    @Override
    public String getName() {
        if (type == TYPE_NORMAL) {
            return "Mesa";
        } else if (type == TYPE_BRYCE) {
            return "Mesa Bryce";
        } else if (type == TYPE_PLATEAU) {
            return "Mesa Plateau";
        } else {
            return "Mesa Plateau M";
        }
    }
}
