
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;

public class RainSplashParticle extends GenericParticle{
	public RainSplashParticle(Vector3 pos){
		super(pos, Particle.TYPE_RAIN_SPLASH);
	}
}
