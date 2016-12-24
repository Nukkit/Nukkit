package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityBeacon;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: Angelic47 Nukkit Project
 */
public class BlockBeacon extends BlockSolid {

    public BlockBeacon() {
        this(0);
    }

    public BlockBeacon(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return BEACON;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Beacon";
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        // TODO handle GUI
        //Server.getInstance().getLogger().info("BlockBeacon.onActivate called");
        return true;
    }

    @Override
    public int[][] getDrops(Item item) {
        return new int[][]{{Item.BEACON, 0, 1}};
    }


    @Override
    public boolean place(Item item, Block block, Block target, int face, double fx, double fy, double fz, Player player) {
        boolean blockSuccess = super.place(item, block, target, face, fx, fy, fz, player);

        if(blockSuccess) {
            CompoundTag nbt = new CompoundTag("")
                    .putString("id", BlockEntity.BEACON)
                    .putInt("x", (int) this.x)
                    .putInt("y", (int) this.y)
                    .putInt("z", (int) this.z);
            new BlockEntityBeacon(this.level.getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);
        }

        return blockSuccess;
    }
}
