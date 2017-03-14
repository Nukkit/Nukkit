package cn.nukkit.item;

/*
 * by kazuemon
 */

public class ItemMap extends Item{

	public ItemMap() {
        this(0, 1);
    }

    public ItemMap(Integer meta) {
        this(meta, 1);
    }

    public ItemMap(Integer meta, int count) {
        super(MAP, meta, count, "Map");
    }

}
