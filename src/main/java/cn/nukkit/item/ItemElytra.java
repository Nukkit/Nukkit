package cn.nukkit.item;

public class ItemElytra extends ItemArmor {
    public ItemElytra() {
        this(0, 1);
    }

    public ItemElytra(Integer meta) {
        this(meta, 1);
    }

    public ItemElytra(Integer meta, int count) {
        super(ELYTRA, meta, count, "Elytra wings");
    }

    @Override
    public int getMaxDurability() {
        return 431;
    }

    @Override
    public boolean isChestplate() {
        return true;
    }
}
