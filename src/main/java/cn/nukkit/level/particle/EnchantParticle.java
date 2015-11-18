
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class EnchantParticle extends GenericParticle{
	public EnchantParticle(Vector3 pos){
		super(pos, Particle.TYPE_MOB_SPELL);
	}
}
