package cn.nukkit.block;

import cn.nukkit.item.Item;

public class Bedrock extends Solid{

	public Bedrock(){ this(0); }

	public Bedrock(int meta) {
		super(Block.BEDROCK, meta);
	}

	public String getName(){
		return "Bedrock";
	}

	public double getHardness(){
		return -1;
	}

	public double getResistance(){
		return 18000000;
	}

	public boolean isBreakable(Item item){
		return false;
	}

}