
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class RedstoneParticle extends GenericParticle{
	public RedstoneParticle(Vector3 pos){
		this(pos, 1);
	}

	public RedstoneParticle(Vector3 pos, int lifetime){
		super(pos, Particle.TYPE_REDSTONE, lifetime);
	}
}
