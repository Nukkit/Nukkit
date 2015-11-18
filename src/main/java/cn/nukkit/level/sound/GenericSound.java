package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.LevelEventPacket;

public class GenericSound extends Sound{
	
	public GenericSound(Vector3 pos, int id, float pitch){
		super.setComponents(pos.x, pos.y, pos.z);
		this.id = id;
		this.pitch = pitch * 1000;
	}
	
	protected float pitch = 0;
	protected int id;
	
	public float getPitch(){
		return this.pitch / 1000;
	}
	
	public void setPitch(float pitch){
		this.pitch = pitch * 1000;
	}
	
	
	public DataPacket[] encode(){
		LevelEventPacket pk = new LevelEventPacket();
		pk.evid = this.id;
		pk.x = (float) this.x;
		pk.y = (float) this.y;
		pk.z = (float) this.z;
		pk.data = (int) this.pitch;
		
		return new DataPacket[]{pk};
	}

}
