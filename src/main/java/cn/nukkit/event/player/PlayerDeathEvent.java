package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.TextContainer;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.item.Item;

public class PlayerDeathEvent extends EntityDeathEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player killer;

    public static HandlerList getHandlers() {
        return handlers;
    }

    private TextContainer deathMessage;
    private boolean keepInventory = false;

    public PlayerDeathEvent(Player player, Player killer, Item[] drops, TextContainer deathMessage) {
        super(player, drops);
        this.deathMessage = deathMessage;
        this.killer = killer;
    }

    public PlayerDeathEvent(Player player, Player killer, Item[] drops, String deathMessage) {
        this(player, killer, drops, new TextContainer(deathMessage));
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }

    public Player getKiller() {
        return this.killer;
    }

    public TextContainer getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(TextContainer deathMessage) {
        this.deathMessage = deathMessage;
    }

    public boolean getKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }
}
