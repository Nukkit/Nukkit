
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class HeartParticle extends GenericParticle{
	public HeartParticle(Vector3 pos){
		this(pos, 0);
	}

	public HeartParticle(Vector3 pos, int scale){
		super(pos, Particle.TYPE_HEART, scale);
	}
}
