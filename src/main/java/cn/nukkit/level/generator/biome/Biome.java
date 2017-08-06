package cn.nukkit.level.generator.biome;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.generator.biome.nether.*;
import cn.nukkit.level.generator.biome.normal.*;
import cn.nukkit.level.generator.populator.Populator;
import cn.nukkit.math.NukkitRandom;

import java.util.ArrayList;

/**
 * author: MagicDroidX
 * Modificated by NycuRO on 05.08.2017
 * Nukkit Project
 */
public abstract class Biome {


    public static final int OCEAN = 0;
    public static final int PLAINS = 1;
    public static final int DESERT = 2;
    public static final int EXTREME_HILLS = 3;
    public static final int FOREST = 4;
    public static final int TAIGA = 5;
    public static final int SWAMPLAND = 6;
    public static final int RIVER = 7;
    public static final int HELL = 8;
    /**
     * 9
     * 10
     */
    public static final int FROZEN_RIVER = 11;
    public static final int ICE_FLATS = 12;
    /** 13 **/
    public static final int MUSHROOM_ISLAND = 14;
    public static final int MUSHROOM_ISLAND_SHORE = 15;
    public static final int BEACHES = 16;
    /**
     * 17
     * 18
     * 19
     * 20
     */
    public static final int JUNGLE = 21;
    /** 22 */
    public static final int JUNGLE_EDGE = 23;
    public static final int DEEP_OCEAN = 24;
    public static final int STONE_BEACH = 25;
    public static final int COLD_BEACH = 26;
    public static final int BIRCH_FOREST = 27;
    /** 28 */
    public static final int ROOFED_FOREST = 29;
    public static final int TAIGA_COLD = 30;
    /** 31 */
    public static final int MEGA_TAIGA = 32;
    /** 33 */
    public static final int EXTREME_HILLS_WITH_TREES = 34;
    public static final int SAVANNA = 35;
    /** 36 */
    public static final int MESA = 37;
    /** 38 */
    public static final int MESA_PLATEAU = 39;
    /**
     * 40 - 126
     */
    public static final int THE_VOID = 127;
    /** 128 */
    public static final int MUTATED_PLAINS = 129;
    public static final int MUTATED_DESERT = 130;
    public static final int MUTATED_EXTREME_HILLS = 131;
    public static final int MUTATED_FOREST = 132;
    public static final int MUTATED_TAIGA = 133;
    public static final int MUTATED_SWAMPLAND = 134;
    /**
     * 135 - 139
     */
    public static final int MUTATED_ICE_FLATS = 140;
    /**
     * 141 - 148
     */
    public static final int MUTATED_JUNGLE = 149;
    /** 150 */
    public static final int MUTATED_JUNGLE_EDGE = 151;
    /**
     * 152 - 156
     */
    public static final int MUTATED_ROOFED_FOREST = 157;
    public static final int MUTATED_TAIGA_COLD = 158;
    /** 159 */
    public static final int MUTATED_REDWOOD_TAIGA = 160;
    /** 161 */
    public static final int MUTATED_EXTREME_HILLS_WITH_TREES = 162;
    public static final int MUTATED_SAVANNA = 163;
    /** 164 */
    public static final int MUTATED_MESA = 165;
    /** 166 */
    public static final int MUTATED_MESA_CLEAR_ROCK = 167;

    public static final int MAX_BIOMES = 256;

    private static final Biome[] biomes = new Biome[MAX_BIOMES];

    private int id;
    private boolean registered = false;

    private final ArrayList<Populator> populators = new ArrayList<>();

    private int minElevation;
    private int maxElevation;

    private Block[] groundCover;

    public double rainfall = 0.5f;
    public double temperature = 0.5f;
    protected int grassColor = 0;
    protected float waterColor = 16777215;

    protected static void register(int id, Biome biome) {
        biome.setId(id);
        biome.grassColor = generateBiomeColor(biome.getTemperature(), biome.getRainfall());
        biomes[id] = biome;
    }

    public static void init() {
        register(OCEAN, new OceanBiome());
        register(PLAINS, new PlainsBiome());
        register(DESERT, new DesertBiome());
        register(EXTREME_HILLS, new ExtremeHillsBiome());
        register(FOREST, new ForestBiome());
        register(TAIGA, new TaigaBiome());
        register(SWAMPLAND, new SwamplandBiome());
        register(RIVER, new RiverBiome());
        register(HELL, new HellBiome());
        /**
         * 9
         * 10
         */
        register(FROZEN_RIVER, new FrozenRiverBiome());
        register(ICE_FLATS, new IcePlainsBiome());
        /** 13 **/
        register(MUSHROOM_ISLAND, new MushroomIslandBiome());
        register(MUSHROOM_ISLAND_SHORE, new MushroomIslandBiome(MushroomIslandBiome.TYPE_SHORE));
        register(BEACHES, new BeachBiome());
        /**
         * 17
         * 18
         * 19
         * 20
         */
        register(JUNGLE, new JungleBiome());
        /** 22 */
        register(JUNGLE_EDGE, new JungleBiome(JungleBiome.TYPE_EDGE));
        register(STONE_BEACH, new BeachBiome(BeachBiome.TYPE_STONE));
        register(DEEP_OCEAN, new OceanBiome(OceanBiome.TYPE_DEEP));
        register(COLD_BEACH, new BeachBiome(BeachBiome.TYPE_COLD));
        register(BIRCH_FOREST, new ForestBiome(ForestBiome.TYPE_BIRCH));
        /** 28 */
        register(ROOFED_FOREST, new ForestBiome(ForestBiome.TYPE_ROOFED));
        register(TAIGA_COLD, new ColdTaigaBiome());
         /** 30 */
        register(MEGA_TAIGA, new TaigaBiome(TaigaBiome.TYPE_MEGA));
         /** 33 */
        register(EXTREME_HILLS_WITH_TREES, new ExtremeHillsPlusBiome());
        register(SAVANNA, new SavannahBiome());
        /** 36 */
        register(MESA, new MesaBiome(MesaBiome.TYPE_NORMAL));
        /** 38 */
        register(MESA_PLATEAU, new MesaBiome(MesaBiome.TYPE_PLATEAU));
        /**
         * 40 - 126
         */
        register(THE_VOID, new VoidBiome());
        /** 128 */
        register(MUTATED_PLAINS, new PlainsBiome(PlainsBiome.TYPE_SUNFLOWERS));
        register(MUTATED_DESERT, new DesertBiome(DesertBiome.TYPE_M));
        register(MUTATED_EXTREME_HILLS, new ExtremeHillsBiome(ExtremeHillsBiome.TYPE_M));
        register(MUTATED_FOREST, new ForestBiome(ForestBiome.TYPE_FLOWER));
        register(MUTATED_TAIGA, new TaigaBiome(TaigaBiome.TYPE_M));
        register(MUTATED_SWAMPLAND, new SwamplandBiome(SwamplandBiome.TYPE_M));
        /**
         * 135 - 139
         */
        register(MUTATED_ICE_FLATS, new IcePlainsBiome(IcePlainsBiome.TYPE_SPIKES));
        /**
         * 141 - 148
         */
        register(MUTATED_JUNGLE, new JungleBiome(JungleBiome.TYPE_M));
        /** 150 */
        register(MUTATED_JUNGLE_EDGE, new JungleBiome(JungleBiome.TYPE_EDGE_M));
        /**
         * 151 - 156
         */
        register(MUTATED_ROOFED_FOREST, new ForestBiome(ForestBiome.TYPE_ROOFED_M));
        register(MUTATED_TAIGA_COLD, new ColdTaigaBiome(ColdTaigaBiome.TYPE_M));
        /** 159 */
        register(MUTATED_REDWOOD_TAIGA, new TaigaBiome(TaigaBiome.TYPE_SPRUCE));
        /** 161 */
        register(MUTATED_EXTREME_HILLS_WITH_TREES, new ExtremeHillsPlusBiome(ExtremeHillsPlusBiome.TYPE_M));
        register(MUTATED_SAVANNA, new SavannahBiome(SavannahBiome.TYPE_M));
        /** 164 */
        register(MUTATED_MESA, new MesaBiome(MesaBiome.TYPE_BRYCE));
        /** 166 */
        register(MUTATED_MESA_CLEAR_ROCK, new MesaBiome(MesaBiome.TYPE_PLATEAU_M));
    }

    public static Biome getBiome(int id) {
        Biome biome = biomes[id];
        return biome != null ? biome : biomes[OCEAN];
    }

    /**
     * Get Biome by name.
     *
     * @param name Name of biome. Name could contain symbol "_" instead of space
     * @return Biome. Null - when biome was not found
     */
    public static Biome getBiome(String name) {
        for (Biome biome : biomes) {
            if (biome != null) {
                if (biome.getName().equalsIgnoreCase(name.replace("_", " "))) return biome;
            }
        }
        return null;
    }

    public void clearPopulators() {
        this.populators.clear();
    }

    public void addPopulator(Populator populator) {
        this.populators.add(populator);
    }

    public void populateChunk(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        for (Populator populator : populators) {
            populator.populate(level, chunkX, chunkZ, random);
        }
    }

    public ArrayList<Populator> getPopulators() {
        return populators;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract String getName();

    public abstract float setWaterColor();

    public int getMinElevation() {
        return minElevation;
    }

    public int getMaxElevation() {
        return maxElevation;
    }

    public void setElevation(int min, int max) {
        this.minElevation = min;
        this.maxElevation = max;
    }

    public Block[] getGroundCover() {
        return groundCover;
    }

    public void setGroundCover(Block[] covers) {
        this.groundCover = covers;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getRainfall() {
        return rainfall;
    }

    private static int generateBiomeColor(double temperature, double rainfall) {
        double x = (1 - temperature) * 255;
        double z = (1 - rainfall * temperature) * 255;
        double[] c = interpolateColor(256, x, z, new double[]{0x47, 0xd0, 0x33}, new double[]{0x6c, 0xb4, 0x93}, new double[]{0xbf, 0xb6, 0x55}, new double[]{0x80, 0xb4, 0x97});
        return ((int) c[0] << 16) | ((int) c[1] << 8) | (int) (c[2]);
    }


    private static double[] interpolateColor(double size, double x, double z, double[] c1, double[] c2, double[] c3, double[] c4) {
        double[] l1 = lerpColor(c1, c2, x / size);
        double[] l2 = lerpColor(c3, c4, x / size);

        return lerpColor(l1, l2, z / size);
    }

    private static double[] lerpColor(double[] a, double[] b, double s) {
        double invs = 1 - s;
        return new double[]{a[0] * invs + b[0] * s, a[1] * invs + b[1] * s, a[2] * invs + b[2] * s};
    }

    abstract public int getColor();

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }
}
