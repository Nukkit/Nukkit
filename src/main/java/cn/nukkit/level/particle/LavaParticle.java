
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

class LavaParticle extends GenericParticle{
	public LavaParticle(Vector3 pos){
		super(pos, Particle.TYPE_LAVA);
	}
}
