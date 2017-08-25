package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

/**
 * Created by CreeperFace on 7.8.2017.
 */
public class BlockJukebox extends BlockSolid {

    public BlockJukebox() {
        this(0);
    }

    public BlockJukebox(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Jukebox";
    }

    @Override
    public int getId() {
        return JUKEBOX;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        //TODO: music
        return false;
    }
}
