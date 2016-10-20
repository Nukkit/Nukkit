package cn.nukkit.item;

public class ItemGoldHorseArmor extends Item {
    public ItemGoldHorseArmor() {
        this(0, 1);
    }

    public ItemGoldHorseArmor(Integer meta) {
        this(meta, 1);
    }

    public ItemGoldHorseArmor(Integer meta, int count) {
        super(GOLD_HORSE_ARMOR, meta, count, "Gold horse armor");
    }
}
