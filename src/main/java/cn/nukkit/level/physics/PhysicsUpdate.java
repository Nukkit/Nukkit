package cn.nukkit.level.physics;

import cn.nukkit.level.range.EffectRange;

/**
 * Nukkit Project
 */
public class PhysicsUpdate {
    private byte x;
    private byte y;
    private byte z;
    private EffectRange range;

    public PhysicsUpdate(int x, int y, int z, EffectRange range) {
        this.x = (byte) x;
        this.y = (byte) y;
        this.z = (byte) z;
        this.range = range;
    }

    public int getX() {
        return x & 0xFF;
    }

    public int getY() {
        return y & 0xFF;
    }

    public int getZ() {
        return z & 0xFF;
    }

    public EffectRange getRange() {
        return range;
    }
}
