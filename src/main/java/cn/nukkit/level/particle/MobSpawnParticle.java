
package cn.nukkit.level.particle;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class MobSpawnParticle extends Particle{
	
	protected int width;
	protected int height;

	public MobSpawnParticle(Vector3 pos){
		this(pos, 0, 0);
	}

	public MobSpawnParticle(Vector3 pos, int width){
		this(pos, width, 0);
	}
	
	public MobSpawnParticle(Vector3 pos, int width, int height){
		super.setComponents(pos.x, pos.y, pos.z);
		this.width = width;
		this.height = height;
	}
	
	public DataPacket[] encode(){
		LevelEventPacket pk = new LevelEventPacket();
		pk.evid = LevelEventPacket.EVENT_PARTICLE_SPAWN;
		pk.x = (float) this.x;
		pk.y = (float) this.y;
		pk.z = (float) this.z;
		pk.data = (this.width & 0xff) + ((this.height & 0xff) << 8);
		
		return new DataPacket[]{pk};
	}
}
