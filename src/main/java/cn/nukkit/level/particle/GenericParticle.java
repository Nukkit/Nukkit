
/*
 * _   _       _    _    _ _
 * | \ | |     | |  | |  (_) |
 * |  \| |_   _| | _| | ___| |_
 * | . ` | | | | |/ / |/ / | __|
 * | |\  | |_| |   <|   <| | |_
 * |_| \_|\__,_|_|\_\_|\_\_|\__|
 */

package cn.nukkit.level.particle;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.math.Vector3;

public class GenericParticle extends Particle{
	
	protected int id;
	protected int data;

	public GenericParticle(Vector3 pos, int id){
		this(pos, id, 0);
	}

	public GenericParticle(Vector3 pos, int id, int data){
		super.setComponents(pos.x, pos.y, pos.z);
		this.id = id & 0xFFF;
		this.data = data;
	}
	
	public DataPacket[] encode(){ //i think it shouldn't return an array
		LevelEventPacket pk = new LevelEventPacket();
		pk.evid = LevelEventPacket.EVENT_ADD_PARTICLE_MASK | this.id;
		pk.x = (float) this.x;
		pk.y = (float) this.y;
		pk.z = (float) this.z;
		pk.data = this.data;
		
		return new DataPacket[]{pk};
	}
}
