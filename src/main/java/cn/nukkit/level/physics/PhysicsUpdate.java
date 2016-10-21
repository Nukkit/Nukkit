package cn.nukkit.level.physics;

import cn.nukkit.level.range.EffectRange;

/**
 * Nukkit Project
 */
public class PhysicsUpdate {
    private int x;
    private int y;
    private int z;
    private EffectRange range;

    public PhysicsUpdate(int x, int y, int z, EffectRange range) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.range = range;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public EffectRange getRange() {
        return range;
    }
}
