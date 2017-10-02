package cn.nukkit.item;

/**
 * Created by Leonidius20 on 02.10.17.
 */
public class ItemChorusFruit extends ItemEdible {
    public ItemChorusFruit() {
        this( 0, 1);
    }
    public ItemChorusFruit(int meta) {
        this(meta, 1);
    }

    public ItemChorusFruit (int meta, int count) {
        super (CHORUS_FRUIT, meta, count, "Chorus Fruit");
    }
}
