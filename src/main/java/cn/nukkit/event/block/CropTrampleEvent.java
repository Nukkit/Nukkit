package cn.nukkit.event.block;

import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * author: MrPowerGamerBR
 * Nukkit Project
 */
public class CropTrampleEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private final Entity whoIsTrampling;

    public CropTrampleEvent(Entity whoIsTrampling, Block block) {
        super(block);
        this.whoIsTrampling = whoIsTrampling;
    }

    public Entity getEntity() {
        return whoIsTrampling;
    }
}
