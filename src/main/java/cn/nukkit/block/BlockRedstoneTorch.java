package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneTorch extends BlockRedstoneTorchUnlit {

    public BlockRedstoneTorch() {
        this(0);
    }

    public BlockRedstoneTorch(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Redstone Torch";
    }

    @Override
    public int getId() {
        return REDSTONE_TORCH;
    }

    @Override
    public int getLightLevel() {
        return 0;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{
                this.toItem()
        };
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            BlockFace below = this.getSide();
        }
    }
}
