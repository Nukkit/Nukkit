package cn.nukkit.item;

public class ItemBeacon extends Item{

    public ItemBeacon() {
        this(0, 1);
    }

    public ItemBeacon(Integer meta) {
        this(meta, 1);
    }

    public ItemBeacon(Integer meta, int count) {
        super(BEACON, 0, count, "Beacon");
    }
}
