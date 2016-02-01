package cn.nukkit.entity.passive;

import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Author: BeYkeRYkt 
 * Nukkit Project
 */
public class EntityChicken extends EntityAnimal {

	public static final int NETWORK_ID = 93;

	public EntityChicken(FullChunk chunk, CompoundTag nbt) {
		super(chunk, nbt);
	}

	@Override
	public float getWidth() {
		return 0.4f;
	}

	@Override
	public float getLength() {
		return 0.4f;
	}

	@Override
	public float getHeight() {
		if (isBaby()) {
			return 0.51f;
		}
		return 0.7f;
	}

	@Override
	public float getEyeHeight() {
		if (isBaby()) {
			return 0.51f;
		}
		return 0.7f;
	}

	@Override
	public String getName() {
		return this.getNameTag();
	}

	@Override
	public Item[] getDrops() {
		return new Item[Item.LEATHER];
	}

	@Override
	public int getNetworkId() {
		return NETWORK_ID;
	}
}
