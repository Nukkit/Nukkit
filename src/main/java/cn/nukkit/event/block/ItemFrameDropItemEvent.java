package cn.nukkit.event.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;

public class ItemFrameDropItemEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private Player player;

    private Item item;

    private BlockEntityItemFrame itemFrame;

    public ItemFrameDropItemEvent(Player player, Block block, BlockEntityItemFrame itemFrame, Item item) {
        super(block);

        this.player = player;
        this.itemFrame = itemFrame;
        this.item = item;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockEntityItemFrame getItemFrame() {
        return itemFrame;
    }

    public Item getItem() {
        return item;
    }
}
