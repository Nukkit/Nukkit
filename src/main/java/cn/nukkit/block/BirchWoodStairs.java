package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.Tool;

public class BirchWoodStairs extends Stair{

	public BirchWoodStairs(){ this(0); }

	public BirchWoodStairs(int meta) {
		super(Block.BIRCH_WOOD_STAIRS, meta);
	}

	public String getName(){
		return "Birch Wood Stairs";
	}

	public int[][] getDrops(Item item){
		return new int[][]{
				{Item.BIRCH_WOOD_STAIRS, 0, 1}
		};
	}

	public double getHardness(){
		return 2;
	}

	public double getResistance(){
		return 15;
	}

	public int getToolType(){
		return Tool.TYPE_AXE;
	}
}