package cn.nukkit.level.generator.populator;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitRandom;

import java.util.Map;
import java.util.Map.Entry;
import cn.nukkit.math.MathHelper;
import cn.nukkit.level.generator.biome.Biome;
//import net.minecraft.world.biome.BiomeMesa;


public class PopulatorMineshaft extends Populator {

    /**
     * Author: Niall Lindsay <Niall7459>
     * Need MESA biome for finish.
     */
    
    private double chance = 0.004D;

    public PopulatorMineshaft()
    {
    }
    
    public function getName()
    {
        return "Mineshaft";
    }
    
    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) { // need mesa
        return this.rand.nextDouble() < this.chance && this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
        /* biome = this.worldObj.getBiomeGenForCoords(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));
        MapGenMineshaft.Type mapgenmineshaft$type = biome instanceof BiomeMesa ? MapGenMineshaft.Type.MESA : MapGenMineshaft.Type.NORMAL;
        return new StructureMineshaftStart(this.worldObj, this.rand, chunkX, chunkZ, mapgenmineshaft$type);*/
    }

    public static enum Type
    {
        NORMAL;

        // MESA HERE
        public static PopulatorMineshaft.Type func_189910_a(int p_189910_0_)
        {
            return p_189910_0_ >= 0 && p_189910_0_ < values().length ? values()[p_189910_0_] : NORMAL;
        }
    }
}
