package cn.nukkit.entity.ai;

import cn.nukkit.math.Vector3;

public interface BlockVisitor {
    public boolean isVisitable(Vector3 from, Vector3 to);
}
