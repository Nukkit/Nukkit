package cn.nukkit.entity.monsters;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.MobEquipmentPacket;

public class Skeleton extends EntityCreature {

    public static final int NETWORK_ID = 34;

    public Skeleton(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public void initEntity() {
        super.initEntity();

        this.setMaxHealth(20);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.65f;
    }

    @Override
    public float getHeight() {
        return 1.8f;
    }

    @Override
    public void spawnTo(Player player) {
        super.spawnTo(player);

        MobEquipmentPacket pk = new MobEquipmentPacket();
        pk.eid = this.getId();
        pk.item = new ItemBow();
        pk.slot = 10;
        pk.selectedSlot = 10;
        player.dataPacket(pk);
    }

    @Override
    public boolean entityBaseTick(int tickDiff) {
        boolean hasUpdate = false;

        hasUpdate = super.entityBaseTick(tickDiff);

        int time = this.getLevel().getTime() % Level.TIME_FULL;
        if (!this.isOnFire() && !this.level.isRaining() && (time < Level.TIME_NIGHT || time > Level.TIME_SUNRISE)) {
            this.setOnFire(100);
        }
        return hasUpdate;
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.BONE), Item.get(Item.ARROW)};               
    }

    public int getKillExperience() {
        return 5; 
    }

}
