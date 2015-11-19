package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

public class IronDoor extends Door{

	public IronDoor(){ this(0); }

	public IronDoor(int meta) {
		super(Block.IRON_DOOR_BLOCK, meta);
	}

	@Override
	public String getName(){
		return "Iron Door Block";
	}

	@Override
	public boolean canBeActivated(){
		return true;
	}

	@Override
	public double getHardness(){
		return 5;
	}

	@Override
	public int getToolType(){
		return Tool.TYPE_PICKAXE;
	}

	@Override
	public int[][] getDrops(Item item){
		if(item.isPickaxe()){
			return new int[][]{
					{Item.ACACIA_DOOR, 0, 1}
			};
		}
		return new int[][]{};
	}
}