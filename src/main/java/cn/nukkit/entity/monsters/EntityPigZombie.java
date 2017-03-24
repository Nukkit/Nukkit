package cn.nukkit.entity.monsters;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemSwordGold;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.entity.mobutils.Drops;

import java.util.ArrayList;
import java.util.List;

public class EntityPigZombie extends EntityCreature {

    public static final int NETWORK_ID = 36;

    public EntityPigZombie(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.72f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public float getEyeHeight() {
        return 1.62f;
    }

    public double getSpeed() {
        return 1.15;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        
        this.fireProof = true;
        setMaxHealth(20);
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = new ItemSwordGold();
        pk.slot = 10;
        pk.selectedSlot = 10;
        player.dataPacket(pk);
    }

    @Override
    public Item[] getDrops() {
        List<Item> drops = new ArrayList<>();
        if (this.lastDamageCause instanceof EntityDamageByEntityEvent) {
            int rottenFlesh = Drops.rand(0, 2); // drops 0-1 rotten flesh
            int goldNuggets = Drops.rand(0, 2); // drops 0-1 gold nugget 
            int goldSword = Drops.rand(0, 101) <= 9 ? 1 : 0; // with a 8,5% chance it's gold sword is dropped
            for (int i=0; i < rottenFlesh; i++) {
                drops.add(Item.get(Item.ROTTEN_FLESH, 0, 1));
            }
            for (int i=0; i < goldNuggets; i++) {
                drops.add(Item.get(Item.GOLD_NUGGET, 0, 1));
            }
            for (int i=0; i < goldSword; i++) {
                drops.add(Item.get(Item.GOLD_SWORD, 0, 1));
            }
        }
        return drops.toArray(new Item[drops.size()]);
    }
    
    public int getKillExperience () {
        return 5; // gain 5 experience
    }

}
