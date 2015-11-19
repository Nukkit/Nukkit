
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class WaterDripParticle extends GenericParticle{
	public WaterDripParticle(Vector3 pos){
		super(pos, Particle.TYPE_DRIP_WATER);
	}
}
