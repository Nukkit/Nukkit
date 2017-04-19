package cn.nukkit.event.painting;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

/**
 * Created by fromgate on 28.06.2016.
 */
public class PaintingPlaceEvent extends PaintingEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private Player player;

    public PaintingPlaceEvent(Player player, EntityPainting painting) {
        this.painting = painting;
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}