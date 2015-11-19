package cn.nukkit.block;

import cn.nukkit.item.Item;

import java.util.Random;

public class Beetroot extends Crops{

	public Beetroot(){ this(0); }

	public Beetroot(int meta) {
		super(Block.BEETROOT_BLOCK, meta);
	}

	public String getName(){
		return "Beetroot Block";
	}

	public int[][] getDrops(Item $item){
		int[][] drops;

		if(this.meta >= 0x07){
			drops = new int[][]{{Item.BEETROOT, 0, 1}, {Item.BEETROOT_SEEDS, 0, new Random().nextInt(3)}};
		}else{
			drops = new int[][]{{Item.BEETROOT_SEEDS, 0, 1}};
		}

		return drops;
	}
}