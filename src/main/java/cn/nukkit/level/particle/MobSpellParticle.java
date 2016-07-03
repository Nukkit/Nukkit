package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

/**
 * author: MrGenga
 * Nukkit Project
 */
public class MobSpellParticle extends GenericParticle{
    public MobSpellParticle(Vector3 pos, int r, int g, int b, int a){
        super(pos, Particle.TYPE_MOB_SPELL, ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff));
    }
}
