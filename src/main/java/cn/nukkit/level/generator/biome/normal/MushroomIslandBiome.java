package cn.nukkit.level.generator.biome.normal;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockMycelium;
import cn.nukkit.level.generator.biome.CaveBiome;
import cn.nukkit.level.generator.biome.NormalBiome;
import cn.nukkit.level.generator.populator.MushroomPopulator;

public class MushroomIslandBiome extends NormalBiome implements CaveBiome {

    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_SHORE = 1;

    public final int type;

    public MushroomIslandBiome() {
        this(TYPE_NORMAL);
    }

    public MushroomIslandBiome(int type) {
        super();
        this.type = type;
        if (type == TYPE_NORMAL) {
            this.setGroundCover(new Block[]{
                    new BlockMycelium(),
                    new BlockDirt(),
                    new BlockDirt(),
                    new BlockDirt(),
                    new BlockDirt()
            });
            MushroomPopulator mushroomPopulator = new MushroomPopulator();
            mushroomPopulator.setBaseAmount(1);
            addPopulator(mushroomPopulator);
            setElevation(60, 70);
            temperature = 0.9f;
            rainfall = 1.0f;
        } else {
            this.setElevation(60, 70);
        }
    }

    @Override
    public String getName() {
        return type == TYPE_NORMAL ? "Mushroom Island" : "Mushroom Island Shore";
    }

    @Override
    public int getSurfaceBlock() {
        return Block.MYCELIUM;
    }

    @Override
    public int getGroundBlock() {
        return Block.DIRT;
    }

    @Override
    public int getStoneBlock() {
        return Block.STONE;
    }

}