
package cn.nukkit.level.particle;

import cn.nukkit.block.Block;
import cn.nukkit.math.Vector3;

public class TerrainParticle extends GenericParticle{
	public TerrainParticle(Vector3 pos, Block b){
		super(pos, Particle.TYPE_TERRAIN, (b.getDamage() << 8) | b.getId());
	}
}
