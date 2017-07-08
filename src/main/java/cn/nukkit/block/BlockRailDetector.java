package cn.nukkit.block;

import cn.nukkit.Server;
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
        super.onUpdate(type);
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            int power = meta;
            
            updateState(power);
        }

        return 0;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        int power = meta;

        updateState(power);
    }

    protected void updateState(int oldStrength) {
        boolean wasPowered = (oldStrength & 0x8) != 0;
        boolean isPowered = false;
        
        for (Entity entity : level.getNearbyEntities(new AxisAlignedBB(
                    getFloorX() + 0.125F,
                    getFloorY(),
                    getFloorZ() + 0.125F,
                    getFloorX() + 0.875F,
                    getFloorY() + 1.124F, // Todo: adjacent
                    getFloorZ() + 0.875F))) {
            if(entity instanceof EntityMinecartAbstract){
                isPowered = true;
            }
            Server.getInstance().getLogger().debug("Entity name: " + entity.getName());
        }

        if (isPowered && !wasPowered) {
            setActive(true);
            Server.getInstance().getLogger().debug("Powered!");
            level.scheduleUpdate(this, this, 1);
            level.scheduleUpdate(this, this.down(), 1);
        }

        if (!isPowered && wasPowered) {
            setActive(false);
            level.scheduleUpdate(this, this, 1);
            level.scheduleUpdate(this, this.down(), 1);
        }

        level.updateComparatorOutputLevel(this);
    }
}
