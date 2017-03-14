package cn.nukkit.item;

/*
 * by kazuemon
 */

public class ItemBlazeRod extends Item{

    public ItemBlazeRod() {
        this(0, 1);
    }

    public ItemBlazeRod(Integer meta) {
        this(meta, 1);
    }

    public ItemBlazeRod(Integer meta, int count) {
        super(BLAZE_ROD, meta, count, "BlazeRod");
    }

}
