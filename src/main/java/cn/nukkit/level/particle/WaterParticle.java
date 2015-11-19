
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class WaterParticle extends GenericParticle{
	public WaterParticle(Vector3 pos){
		super(pos, Particle.TYPE_WATER_WAKE);
	}
}
