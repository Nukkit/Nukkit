package cn.nukkit.entity;

import cn.nukkit.inventory.InvetoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
        
/**
 * Author: PikyCZ
 * Nukkit Project
 * Todo: Width,Lenght,Height
 */
        
public class EntityEnderman extends EntityCreature {
    
 public static final int NETWORK_ID = 38;
    
 public EntityEnderman(FullChunk chunk, CompoundTag nbt) {
      super(chunk, nbt);
}
    
 @Override
 public int getNetworkId(){
     return NETWORK_ID;
 }

 @Override
 public float getWidth(){
     return ?.?f;
 }

 @Override
 public float getLenght(){
     return ?.?f;
 }

 @Override
 public float getHeight(){
     return ?.?f;
 }

 @Override
 public String getName(){
     return this.getName Tag();
 }

 @Override
 public item[] getDrops(){
     return new Item[]{item.get(377,0,1)};
 }

 @Override
 public void initEntity(){
     super.initEntity();
     setMaxHealt(7);
 }
}
