package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class EndermanTeleportSound extends GenericSound{
	public EndermanTeleportSound(Vector3 pos){
		this(pos, 0);
	}

	public EndermanTeleportSound(Vector3 pos, float pitch){
		super(pos, LevelEventPacket.EVENT_SOUND_ENDERMAN_TELEPORT, pitch);
	}
}
