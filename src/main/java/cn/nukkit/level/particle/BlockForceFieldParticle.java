package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class BlockForceFieldParticle extends GenericParticle {

    public BlockForceFieldParticle(Vector3 pos, int data = 0) {
        super(pos, Particle.TYPE_BLOCK_FORCE_FIELD, data);
    }

}
