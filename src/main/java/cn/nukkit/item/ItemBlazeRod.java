package cn.nukkit.item;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ItemBlazeRod extends Item {

    public ItemBlazeRod() {
        this(0, 1);
    }

    public ItemBlazeRod(Integer meta) {
        this(meta, 1);
    }

    public ItemBlazeRod(Integer meta, int count) {
        super(EGG, meta, count, "BlazeRod");
    }
}
