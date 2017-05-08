package cn.nukkit.tile;

/**
 * 
 * @author Justin
 *
 */

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;


public  class Skull extends Spawnable{

	 public Skull(FullChunk chunk, CompoundTag nbt) {
	        super(chunk, nbt);
	        if (!nbt.contains("SkullType")){
	        	nbt.putString("SkullType", "0");
	        }
	        this.namedTag = nbt;
	    }

	    @Override
	    public void saveNBT() {
	        super.saveNBT();
	        this.namedTag.remove("Creator");
	    }

	    @Override
	    public CompoundTag getSpawnCompound() {
	        return new CompoundTag()
	                .putString("id", Tile.SKULL)
	        		.put("SkullType", this.namedTag.get("SkullType"))
	                .putInt("x", (int) this.x)
	                .putInt("y", (int) this.y)
	                .putInt("z", (int) this.z)
	        		.put("Rot", this.namedTag.get("Rot"));
}
	    
	    public String[] getSkullType() {
	        return new String[]{
	                this.namedTag.getString("SkullType"),
	        
	    };
}
}
