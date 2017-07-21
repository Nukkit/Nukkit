package cn.nukkit.entity.item;

import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class EntityVehicle extends EntityInteractable implements EntityRideable {
    
    // Ahm this wrong...
    // Maybe someone can get the (REAL) one?
    public static final int DATA_DAMAGE_TAKEN = 19; 

    public EntityVehicle(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public int getRollingAmplitude() {
        return this.getDataPropertyInt(DATA_HURT_TIME);
    }

    public void setRollingAmplitude(int time) {
        this.setDataProperty(new IntEntityData(DATA_HURT_TIME, time));
    }

    public int getRollingDirection() {
        return this.getDataPropertyInt(DATA_HURT_DIRECTION);
    }

    public void setRollingDirection(int direction) {
        this.setDataProperty(new IntEntityData(DATA_HURT_DIRECTION, direction));
    }

    public float getDamage() {
        return this.getDataPropertyFloat(EntityVehicle.DATA_DAMAGE_TAKEN);
    }

    public void setDamage(float damage) {
        this.setDataProperty(new FloatEntityData(EntityVehicle.DATA_DAMAGE_TAKEN, damage));
    }
    
    @Override
    public String getInteractButton() {
        return "mount";
    }
    
    @Override
    public boolean canDoInteraction(){
        return linkedEntity != null;
    }
}
