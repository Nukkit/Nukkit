package cn.nukkit.item;

/**
 * Created by Leonidius20 on 02.10.17.
 */
public class ItemChorusFruitPopped extends Item {

    public ItemChorusFruitPopped() {
        this( 0, 1);
    }
    public ItemChorusFruitPopped(int meta) {
        this(meta, 1);
    }

    public ItemChorusFruitPopped (int meta, int count) {
        super (POPPED_CHORUS_FRUIT, meta, count, "Popped Chorus Fruit");
    }

}
