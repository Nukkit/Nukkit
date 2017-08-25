package cn.nukkit.inventory.transaction;

import cn.nukkit.item.Item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public interface Transaction {

    int TYPE_CONTAINER = 0;
    int TYPE_GLOBAL = 1;
    int TYPE_WORLD_INTERACTON = 2;
    int TYPE_CREATIVE = 3;
    int TYPE_CRAFT = 99999;

    int getSlot();

    Item getSourceItem();

    Item getTargetItem();

    long getCreationTime();

    int getType();
}
