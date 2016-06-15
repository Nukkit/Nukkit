package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.item.EntityMap;
import cn.nukkit.level.Level;

/**
 * author: boybook
 * Nukkit Project
 */
public class ItemEmptyMap extends Item {

    public ItemEmptyMap() {
        this(0);
    }

    public ItemEmptyMap(int meta) {
        this(meta, 1);
    }

    public ItemEmptyMap(long meta, int count) {
        super(EMPTY_MAP, 0, count, "Empty Map");
    }

}
