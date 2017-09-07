package cn.nukkit.block.redstoneBehavior;

import cn.nukkit.math.AxisAlignedBB;

/**
 * Created by NycuRO on 07.08.2017
 * Credits: Steadfast2
 */
public abstract class FlowableRedstoneComponent extends TransparentRedstoneComponent {

    public boolean canBeFlowedInto() {
        return true;
    }

    public double getHardness() {
        return 0;
    }

    public double getResistance() {
        return 0;
    }

    public boolean isSolid() {
        return false;
    }

    public AxisAlignedBB getBoundingBox() {
        return null;
    }
}