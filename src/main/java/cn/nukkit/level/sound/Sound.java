package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;

public abstract class Sound extends Vector3{

	abstract public DataPacket[] encode();

}
