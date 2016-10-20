package cn.nukkit.item;

public class ItemSaddle extends Item {
    public ItemSaddle() {
        this(0, 1);
    }

    public ItemSaddle(Integer meta) {
        this(meta, 1);
    }

    public ItemSaddle(Integer meta, int count) {
        super(SADDLE, meta, count, "Saddle");
    }
}
