package cn.nukkit.block;

/**
 * author: Justin
 */

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.Player;
import cn.nukkit.tile.Tile;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.tile.Skull;


public class SkullBlock extends Transparent{
	public static final int SKELATON_SKULL = 0;
    public static final int WITHER_SKELETON_SKULL = 1;
    public static final int ZOMBIE_HEAD = 2;
    public static final int HEAD = 3;
    public static final int CREEPER_HEAD = 4;

	public SkullBlock() {
        this(0);
    }

	public SkullBlock(int meta) {
        super(meta);
    }


    @Override
    public int getId() {
        return SKULL_BLOCK;
    }

    @Override
    public double getHardness() {
        return 1;
    }
	
    @Override
    public boolean isSolid(){
		return false;
	}
    
    @Override
    public String getName() {
        String[] names = new String[]{
                "Skeleton Skull",
                "Wither Skeleton Skull",
                "Zombie Head",
                "Head",
                "Creeper Head"
        };

        return names[this.meta & 0x04];
    }
    

	@Override
	public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz) {
	    return this.place(item, block, target, face, fx, fy, fz, null);
	}
	
	@Override
	public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
	    short[] faces = new short[]{
	            0,75,
	            0,5,
	            0,75,
	            0b0000,75,
	            0b0000,5,
	            0b0000,75
	    };
	
	    this.meta = ((this.meta & 0x04) | faces[face]);
	    this.getLevel().setBlock(block, this, true, true);
	
	    return true;
	}
	
	public double getResistance(){
		return 5;
	}
	@Override
	public int[][] getDrops(Item item) {
	    return new int[][]{new int[]{this.getId(), this.meta & 0x04, 1}};
	}
	
	@Override
	public int getToolType() {
	    return Tool.TYPE_PICKAXE;
	} 
	}
