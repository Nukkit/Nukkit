
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class EntityFlameParticle extends GenericParticle{
	public EntityFlameParticle(Vector3 pos){
		super(pos, Particle.TYPE_MOB_FLAME);
	}
}
