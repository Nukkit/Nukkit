
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class InkParticle extends GenericParticle{
	public InkParticle(Vector3 pos){
		this(pos, 0);
	}

	public InkParticle(Vector3 pos, int scale){
		super(pos, Particle.TYPE_INK, scale);
	}
}
