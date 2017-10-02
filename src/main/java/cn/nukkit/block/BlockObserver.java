package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.metadata.BlockMetadataStore;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Leonidius20 on 29.09.17.
 */
public class BlockObserver extends BlockSolid {

    public BlockObserver (){
        super (0);
    }

    public BlockObserver (int meta){
        super (meta);
    }

    @Override
    public String getName() {
        return "Observer";
    }

    @Override
    public int getId() {
        return OBSERVER;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz) {
        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {

        this.getLevel().setBlock(block, this, true, true);
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
           //TODO: Make observer mechanics
        }
        return 0;
    }
}
