package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.Player;

import java.util.Random;

public class BlockItemFrame extends BlockTransparent{

    protected int id = Item.ITEM_FRAME;

    @Override
    public int getId(){
        return id;
    }
    
    public BlockItemFrame(){
        this(0);
    }
    
    public BlockItemFrame(int meta){
        super(meta);
        this.meta = meta;
    }
    
    @Override
    public String getName(){
        return "Item Frame";
    }
    
    @Override
    public boolean canBeActivated(){
        return true;
    }
    
    @Override
    public boolean onActivate(Item item, Player player){
        BlockEntityItemFrame tile = (BlockEntityItemFrame) this.getLevel().getBlockEntity(this);
        if (!(tile instanceof BlockEntityItemFrame)){
            CompoundTag nbt = new CompoundTag("");
            nbt.put("id", new StringTag("id", "ItemFrame"));
            nbt.put("x", new IntTag("x", (int) x));
            nbt.put("y", new IntTag("y", (int) y));
            nbt.put("z", new IntTag("z", (int) z));
            nbt.put("ItemRotation", new ByteTag("ItemRotation", 0));
            nbt.put("ItemDropChance", new FloatTag("ItemDropChance", (float) 1.0));
            BlockEntity.createBlockEntity("ItemFrame", this.getLevel().getChunk((int) x >> 4, (int) z >> 4), nbt);
        }
        if (tile.getItem().getId() == 0){
            tile.setItem(Item.get(item.getId(), item.getDamage(), 1));
            if (player instanceof Player){
                if (player.isSurvival()){
                    int count = item.getCount();
                    if (--count <= 0){
                        player.getInventory().setItemInHand(Item.get(Item.AIR));
                        return true;
                    }

                    item.setCount(count);
                    player.getInventory().setItemInHand(item);
                }
            }
        }
        else {
            int itemRot = tile.getItemRotation();
            if (itemRot == 7) itemRot = 0;
            else itemRot++;
            tile.setItemRotation(itemRot);
        }

        return true;
    }

    @Override
    public boolean onBreak(Item item){
        this.getLevel().setBlock(this, new BlockAir(), false);
        return true;
    }

    public int[][] getDrops(Item item){
        if (this.getLevel() == null){
            int[][] array = {};
            return array;
        }
        BlockEntityItemFrame tile = (BlockEntityItemFrame) this.getLevel().getBlockEntity(this);
        if (!(tile instanceof BlockEntityItemFrame)){
            int[][] array = {{389, 0, 1}};
            return array;
        }
        Random r = new Random();
        int next = r.nextInt(100);
        if (next <= (tile.getItemDropChance() * 100)){
            int[][] array = {{389, 0, 1}, {tile.getItem().getId(), tile.getItem().getDamage(), 1}};
            return array;
        }
        int[][] array = {{389, 0, 1}};
        return array;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz){
        return this.place(item, block, target, face, fx, fy, fz, null);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player){
        if (target.isTransparent() == false && face > 1 && block.isSolid() == false){
            int[] faces = {
                    0,
                    0,
                    3,
                    2,
                    1,
                    0
            };
            this.meta = faces[face];
            this.getLevel().setBlock(block, this, true, true);
            CompoundTag nbt = new CompoundTag("");
            nbt.put("id", new StringTag("id", "ItemFrame"));
            nbt.put("x", new IntTag("x", (int) x));
            nbt.put("y", new IntTag("y", (int) y));
            nbt.put("z", new IntTag("z", (int) z));
            nbt.put("ItemRotation", new ByteTag("ItemRotation", 0));
            nbt.put("ItemDropChance", new FloatTag("ItemDropChance", (float) 1.0));
            BlockEntity.createBlockEntity("ItemFrame", this.getLevel().getChunk((int) x >> 4, (int) z >> 4), nbt);
            return true;
        }
        return false;
    }
    
    
}
