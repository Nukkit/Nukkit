package cn.nukkit.inventory;

/**
 * @author CreeperFace
 */
public class BigCraftingGrid extends CraftingGrid {

    public BigCraftingGrid(InventoryHolder holder) {
        super(holder);
    }

    @Override
    public int getSize() {
        return 9;
    }
}
