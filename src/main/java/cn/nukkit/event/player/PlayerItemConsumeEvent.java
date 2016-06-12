package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;

/**
 * Called when a player eats something
 */
public class PlayerItemConsumeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private Item item;
    private boolean fastEat;

    public PlayerItemConsumeEvent(Player player, Item item) {
        this(player, item, false);
    }

    public PlayerItemConsumeEvent(Player player, Item item, boolean fastEat) {
        this.player = player;
        this.item = item;
        this.fastEat = fastEat;
    }

    public Item getItem() {
        return this.item.clone();
    }

    public boolean isFastEat(){
        return fastEat;
    }
}
