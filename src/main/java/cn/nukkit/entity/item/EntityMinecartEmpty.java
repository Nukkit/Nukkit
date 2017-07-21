package cn.nukkit.entity.item;

import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.EnumMinecart;

/**
 * Created by Snake1999 on 2016/1/30.
 * Package cn.nukkit.entity.item in project Nukkit.
 */
public class EntityMinecartEmpty extends EntityMinecartAbstract {

    public static final int NETWORK_ID = 84;

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    public EntityMinecartEmpty(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }
    
    @Override
    public EnumMinecart getType() {
        return EnumMinecart.valueOf(0);
    }

    @Override
    protected void activate(int x, int y, int z, boolean flag) {
        // Some new info: Minecart will add some rolling amplitude if moving trough
        // Activated rail
        if (flag) {
            if (this.riding != null) {
                mountEntity(riding);
            }
            if (getRollingAmplitude() == 0) {
                setRollingAmplitude(10);
                setDamage(50);
            }
        }
    }
}
