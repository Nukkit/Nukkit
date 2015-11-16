package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

public class OakDoor extends Door{

	public OakDoor(){ this(0); }

	public OakDoor(int meta) {
		super(Block.OAK_DOOR_BLOCK, meta);
	}

	@Override
	public String getName(){
		return "Oak Door Block";
	}

	@Override
	public boolean canBeActivated(){
		return true;
	}

	@Override
	public double getHardness(){
		return 3;
	}

	@Override
	public int getToolType(){
		return Tool.TYPE_AXE;
	}

	@Override
	public int[][] getDrops(Item item){
		return new int[][]{
				{Item.OAK_DOOR, 0, 1}
		};
	}
}