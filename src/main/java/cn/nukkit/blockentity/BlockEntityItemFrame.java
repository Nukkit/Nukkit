package cn.nukkit.blockentity;

import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.*;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.NBTIO;

public class BlockEntityItemFrame extends BlockEntitySpawnable {

    public BlockEntityItemFrame(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
        if (!nbt.contains("Item")) {
            nbt.put("Item", NBTIO.putItemHelper(Item.get(Item.AIR)));
            nbt.get("Item").setName("Item");
        }
        if (!nbt.contains("ItemRotation")){
            nbt.put("ItemRotation", new ByteTag("ItemRotation", 0));
        }
        if (!nbt.contains("ItemDropChance")){
            nbt.put("ItemDropChance", new FloatTag("ItemDropChance", (float) 1.0));
        }
    }

    @Override
    public boolean isBlockEntityValid() {
        return true;
    }

    @Override
    public String getName(){
        return "Item Frame";
    }

    public int getItemRotation(){
        return this.namedTag.getInt("ItemRotation");
    }

    public void setItemRotation(int itemRotation){
        this.namedTag.put("ItemRotation", new ByteTag("ItemRotation", itemRotation));
        this.setChanged();
    }

    public Item getItem(){
        return NBTIO.getItemHelper((CompoundTag) this.namedTag.get("Item"));
    }

    public void setItem(Item item){
        setItem(item, true);
    }

    public void setItem(Item item, boolean setChanged){
        CompoundTag nbtItem = NBTIO.putItemHelper(item);
        nbtItem.setName("Item");
        this.namedTag.put("Item", nbtItem);
        if (setChanged) this.setChanged();
    }

    public float getItemDropChance(){
        return this.namedTag.getFloat("ItemDropChance");
    }

    public void setItemDropChance(){
        setItemDropChance((float) 1.0);
    }

    public void setItemDropChance(float chance){
        this.namedTag.put("ItemDropChance", new FloatTag("ItemDropChance", chance));
    }

    private void setChanged(){
        spawnToAll();
        if (chunk instanceof FullChunk){
            chunk.setChanged();
            level.clearChunkCache(chunk.getX(), chunk.getZ());
        }
    }

    @Override
    public CompoundTag getSpawnCompound(){
        if (!this.namedTag.contains("Item")) this.setItem(Item.get(Item.AIR), false);
        CompoundTag nbtItem = (CompoundTag) this.namedTag.get("Item").copy();
        nbtItem.setName("Item");
        if (nbtItem.getInt("id") == 0){
            CompoundTag tag = new CompoundTag("");
            tag.put("id", new StringTag("id", "ItemFrame"));
            tag.put("x", new IntTag("x", (int) x));
            tag.put("y", new IntTag("y", (int) y));
            tag.put("z", new IntTag("z", (int) z));
            tag.put("ItemRotation", new ByteTag("ItemRotation", 0));
            tag.put("ItemDropChance", new FloatTag("ItemDropChance", getItemDropChance()));
            return tag;
        }
        else {
            CompoundTag tag = new CompoundTag("");
            tag.put("id", new StringTag("id", "ItemFrame"));
            tag.put("x", new IntTag("x", (int) x));
            tag.put("y", new IntTag("y", (int) y));
            tag.put("z", new IntTag("z", (int) z));
            tag.put("Item", nbtItem);
            tag.put("ItemRotation", new ByteTag("ItemRotation", getItemRotation()));
            tag.put("ItemDropChance", new FloatTag("ItemDropChance", getItemDropChance()));
            return tag;
        }
    }

}
