package cn.nukkit.event.painting;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class PaintingBreakEvent extends PaintingEvent implements Cancellable{
    private static final HandlerList handlers = new HandlerList();
    Player player;

    public PaintingBreakEvent(Player player, EntityPainting entityPainting) {
        this.painting = entityPainting;
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
