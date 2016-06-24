package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public abstract class BlockButton extends BlockTransparent {

    public BlockButton() {
        this(0);
    }

    public BlockButton(int meta) {
        super(meta);
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz) {
        return true;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        return true;
    }
}