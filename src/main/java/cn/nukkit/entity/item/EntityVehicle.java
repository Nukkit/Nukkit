package cn.nukkit.entity.item;

import cn.nukkit.entity.EntityInteractable;
import cn.nukkit.entity.EntityRideable;
import cn.nukkit.entity.data.FloatEntityData;
import cn.nukkit.entity.data.IntEntityData;
import cn.nukkit.event.vehicle.VehicleCreateEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.Random;

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
    public boolean canDoInteraction() {
        return linkedEntity != null;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (justCreated) {
            VehicleCreateEvent event = new VehicleCreateEvent(this);
            getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                kill();
                return false;
            }
            justCreated = false;
        }

        // The rolling amplitude
        if (getRollingAmplitude() > 0) {
            setRollingAmplitude(getRollingAmplitude() - 1);
        }
        // The damage token
        if (getDamage() > 0) {
            setDamage(getDamage() - 1);
        }
        // A killer task
        if (y < -16) {
            kill();
        }
        // Movement code
        updateMovement();
        return true;
    }

    public boolean performHurtAnimation(float damage) {
        // Vehicle does not respond hurt animation on packets
        // It only respond on vehicle data flags. Such as these
        setRollingAmplitude(10);
        setRollingDirection(renderSideBySideAnimation());
        setDamage(getDamage() + damage * 10.0F);
        return true;
    }
    
    public int renderSideBySideAnimation() {
        BlockFace currentFace = getDirection();
        BlockFace nextFace = BlockFace.fromHorizontalIndex(getRollingDirection());
        Random random = new Random();
        switch (currentFace) {
            case NORTH:
            case SOUTH:
                if (nextFace == BlockFace.WEST || nextFace == BlockFace.EAST) {
                    return nextFace.getOpposite().getIndex();
                }

                BlockFace next = random.nextBoolean() ? BlockFace.EAST : BlockFace.WEST;
                return next.getIndex();
            case WEST:
            case EAST:
                if (nextFace == BlockFace.NORTH || nextFace == BlockFace.SOUTH) {
                    return nextFace.getOpposite().getIndex();
                }
                BlockFace Op2 = random.nextBoolean() ? BlockFace.NORTH : BlockFace.SOUTH;
                return Op2.getIndex();
            default :
                // This should never happend
                return 0;
        }
    }
}
