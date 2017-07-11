package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityMinecartAbstract;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;

/**
 * Created on 2015/11/22 by CreeperFace.
 * Package cn.nukkit.block in project Nukkit .
 */
public class BlockRailDetector extends BlockRail {

    public BlockRailDetector() {
        this(0);
        canBePowered = true;
    }

    public BlockRailDetector(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DETECTOR_RAIL;
    }

    @Override
    public String getName() {
        return "Detector Rail";
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getWeakPower(BlockFace side) {
        return (meta & 0x8) != 0 ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace side) {
        return (meta & 0x8) == 0 ? 0 : (side == BlockFace.UP ? 15 : 0);
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {            
            updateState();
            return Level.BLOCK_UPDATE_SCHEDULED;
        }
        return super.onUpdate(type);
    }

    @Override
    public void onEntityCollide(Entity entity) {
        updateState();
    }

    protected void updateState() {
        boolean wasPowered = (meta & 0x8) != 0;
        boolean isPowered = false;

        for (Entity entity : level.getNearbyEntities(new AxisAlignedBB(
                getFloorX() + 0.125D,
                getFloorY(),
                getFloorZ() + 0.125D,
                getFloorX() + 0.875D,
                getFloorY() + 1.125D, // Todo: adjacent
                getFloorZ() + 0.875D))) {
            if (entity instanceof EntityMinecartAbstract) {
                isPowered = true;
            }
        }

        if (isPowered && !wasPowered) {
            setActive(true);
            level.scheduleUpdate(this, this, 0);
            level.scheduleUpdate(this, this.down(), 0);
        }

        if (!isPowered && wasPowered) {
            setActive(false);
            level.scheduleUpdate(this, this, 0);
            level.scheduleUpdate(this, this.down(), 0);
        }

        level.updateComparatorOutputLevel(this);
    }
}
