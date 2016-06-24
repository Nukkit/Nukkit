package cn.nukkit.blockentity;

import cn.nukkit.block.BlockAir;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.*;

public class BlockEntityItemFrame extends BlockEntitySpawnable {

    public BlockEntityItemFrame(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        if (!nbt.contains("Item")) {
            nbt.putCompound("Item", NBTIO.putItemHelper(new ItemBlock(new BlockAir())));
        }

        if (!nbt.contains("ItemRotation")) {
            nbt.putByte("ItemRotation", 0);
        }

        if (!nbt.contains("ItemDropChance")) {
            nbt.putFloat("ItemDropChance", 1.0f);
        }
    }

    @Override
    public String getName() {
        return "Item Frame";
    }

    @Override
    public boolean isBlockEntityValid() {
        return getBlock().getId() == Item.ITEM_FRAME;
    }

    public int getItemRotation() {
        return namedTag.getByte("ItemRotation");
    }

    public void setItemRotation(int itemRotation) {
        namedTag.putByte("ItemRotation", itemRotation);
        setChanged();
    }

    public Item getItem() {
        return NBTIO.getItemHelper(namedTag.getCompound("Item"));
    }

    public void setItem(Item item) {
        setItem(item, true);
    }

    public void setItem(Item item, boolean setChanged) {
        namedTag.putCompound("Item", NBTIO.putItemHelper(item));
        if (setChanged) setChanged();
    }

    public float getItemDropChance() {
        return namedTag.getFloat("ItemDropChance");
    }

    public void setItemDropChance(float chance) {
        namedTag.putFloat("ItemDropChance", chance);
    }

    private void setChanged() {
        spawnToAll();

        if (chunk != null) {
            chunk.setChanged();
        }
    }

    @Override
    public CompoundTag getSpawnCompound() {
        if (!namedTag.contains("Item")) setItem(new ItemBlock(new BlockAir()), false);

        CompoundTag nbtItem = namedTag.getCompound("Item").copy();
        nbtItem.setName("Item");

        if (nbtItem.getShort("id") == 0) {
            return new CompoundTag()
                    .putString("id", BlockEntity.ITEM_FRAME)
                    .putInt("x", (int) this.x)
                    .putInt("y", (int) this.y)
                    .putInt("z", (int) this.z)
                    .putCompound("Item", NBTIO.putItemHelper(new ItemBlock(new BlockAir())))
                    .putByte("ItemRotation", 0)
                    .putFloat("ItemDropChance", getItemDropChance());
        } else {
            return new CompoundTag()
                    .putString("id", BlockEntity.ITEM_FRAME)
                    .putInt("x", (int) this.x)
                    .putInt("y", (int) this.y)
                    .putInt("z", (int) this.z)
                    .putCompound("Item", nbtItem)
                    .putByte("ItemRotation", this.getItemRotation())
                    .putFloat("ItemDropChance", this.getItemDropChance());
        }
    }
}
