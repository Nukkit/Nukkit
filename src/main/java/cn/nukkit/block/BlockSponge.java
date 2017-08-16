package cn.nukkit.block;

import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockSponge extends BlockSolid {

    public static final int DRY = 0;
    public static final int WET = 1;

    public BlockSponge() {
        this(0);
    }

    public BlockSponge(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return SPONGE;
    }

    @Override
    public double getHardness() {
        return 0.6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    public void absorbWater() {
        int range = 3;
        for (int xx = -range; xx <= range; xx++) {
            for (int yy = -range; yy <= range; yy++) {
                for (int zz = -range; zz <= range; zz++) {
                    Vector3 pos = new Vector3(this.x + xx, this.y + yy, this.z + zz);

                    int block = getLevel().getBlockIdAt(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
                    if (block == Block.WATER) this.getLevel().setBlock(pos, new BlockAir(), true, true);
                    if (block == Block.STILL_WATER) this.getLevel().setBlock(pos, new BlockAir(), true, true);
                }
            }
        }
    }

    @Override
    public int onUpdate(int type) {
        if (this.meta == 0) {
            if (type == Level.BLOCK_UPDATE_NORMAL) {
                for (BlockFace face : BlockFace.values()) {
                    int id = this.level.getBlockIdAt(this.getFloorX() + face.getXOffset(), this.getFloorY() + face.getYOffset(), this.getFloorZ() + face.getZOffset());

                    if (id == WATER || id == STILL_WATER) {
                        this.absorbWater();
                        this.level.setBlock(this, new BlockSponge(1), true, true);
                        return Level.BLOCK_UPDATE_NORMAL;
                    }
                }
            }
        }

        return 0;
    }

    @Override
    public String getName() {
        String[] names = new String[]{
                "Sponge",
                "Wet sponge"
        };
        return names[this.meta & 0x07];
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.CLOTH_BLOCK_COLOR;
    }
}