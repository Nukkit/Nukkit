package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.EntityEventPacket;

/**
 * Created by Snake1999 on 2016/1/30.
 * Package cn.nukkit.entity.item in project Nukkit.
 */
public class EntityMinecartEmpty extends EntityMinecartAbstract {

    public static final int NETWORK_ID = 84;

    public static final int DATA_VEHICLE_DISPLAY_BLOCK = 20;
    public static final int DATA_VEHICLE_DISPLAY_DATA = 20;
    public static final int DATA_VEHICLE_DISPLAY_OFFSET = 21;
    public static final int DATA_VEHICLE_CUSTOM_DISPLAY = 22;


    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityMinecartEmpty(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public boolean attack(EntityDamageEvent source) {
        if (super.attack(source)) {
            EntityEventPacket pk = new EntityEventPacket();
            pk.eid = this.id;
            pk.event = EntityEventPacket.HURT_ANIMATION;
            for (Player aPlayer : this.getLevel().getPlayers().values()) {
                aPlayer.dataPacket(pk);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getMineId() {
        return 0;
    }

}
