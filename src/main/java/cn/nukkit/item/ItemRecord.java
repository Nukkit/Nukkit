package cn.nukkit.item;

/**
 * Created by CreeperFace on 7.8.2017.
 */
public class ItemRecord extends Item {

    public ItemRecord() {
        this(0, 1);
    }

    public ItemRecord(Integer meta) {
        this(meta, 1);
    }

    public ItemRecord(Integer meta, int count) {
        super(meta, count); //TODO: records
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
