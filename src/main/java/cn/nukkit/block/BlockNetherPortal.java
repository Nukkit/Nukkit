package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockFace.Axis;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.BlockColor;

/**
 * Created on 2016/1/5 by xtypr.
 * Package cn.nukkit.block in project nukkit .
 * The name NetherPortalBlock comes from minecraft wiki.
 */
public class BlockNetherPortal extends BlockFlowable {

    public BlockNetherPortal() {
        this(0);
    }

    public BlockNetherPortal(int meta) {
        super(0);
    }

    @Override
    public String getName() {
        return "Nether Portal Block";
    }

    @Override
    public int getId() {
        return NETHER_PORTAL;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public int getLightLevel() {
        return 11;
    }

    @Override
    public boolean onBreak(Item item) {
        boolean result = super.onBreak(item);
        for (BlockFace face : BlockFace.values()) {
            Block b = this.getSide(face);
            if (b != null) {
                if (b instanceof BlockNetherPortal) {
                    result &= b.onBreak(item);
                }
            }
        }
        return result;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(
                this.x,
                this.y,
                this.z,
                this.x + 1,
                this.y + 1,
                this.z + 1
        );
    }

    public static boolean trySpawnPortal(Level level, Vector3 pos) {
        return trySpawnPortal(level, pos, false);
    }

    public static boolean trySpawnPortal(Level level, Vector3 pos, boolean force) {
        PortalBuilder builder = new PortalBuilder(level, pos, Axis.X, force);

        if (builder.isValid() && builder.portalBlockCount == 0) {
            builder.placePortalBlocks();
            return true;
        } else {
            builder = new PortalBuilder(level, pos, Axis.Z, force);

            if (builder.isValid() && builder.portalBlockCount == 0) {
                builder.placePortalBlocks();
                return true;
            } else {
                return false;
            }
        }
    }

    public static class PortalBuilder {

        private final Level level;
        private final Axis axis;
        private final BlockFace rightDir;
        private final BlockFace leftDir;
        private int portalBlockCount;
        private Vector3 bottomLeft;
        private int height;
        private int width;

        private boolean force;

        public PortalBuilder(Level level, Vector3 pos, Axis axis, boolean force) {
            this.level = level;
            this.axis = axis;
            this.force = force;

            if (axis == Axis.X) {
                this.leftDir = BlockFace.EAST;
                this.rightDir = BlockFace.WEST;
            } else {
                this.leftDir = BlockFace.NORTH;
                this.rightDir = BlockFace.SOUTH;
            }


            for (Vector3 blockpos = pos; pos.getY() > blockpos.getY() - 21 && pos.getY() > 0 && this.isEmptyBlock(getBlockId(pos.down())); pos = pos.down()) {
                ;
            }

            int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;

            if (i >= 0) {
                this.bottomLeft = pos.getSide(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);

                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(Vector3 pos, BlockFace dir) {
            int i;

            for (i = 0; i < 22; ++i) {
                Vector3 v = pos.getSide(dir, i);

                if (!this.isEmptyBlock(getBlockId(v)) || getBlockId(v.down()) != OBSIDIAN) {
                    break;
                }
            }

            return getBlockId(pos.getSide(dir, i)) == OBSIDIAN ? i : 0;
        }

        public int getHeight() {
            return this.height;
        }

        public int getWidth() {
            return this.width;
        }

        protected int calculatePortalHeight() {

            loop:
            for (this.height = 0; this.height < 21; ++this.height) {
                for (int i = 0; i < this.width; ++i) {
                    Vector3 blockpos = this.bottomLeft.getSide(this.rightDir, i).up(this.height);
                    int block = getBlockId(blockpos);

                    if (!this.isEmptyBlock(block)) {
                        break loop;
                    }

                    if (block == NETHER_PORTAL) {
                        ++this.portalBlockCount;
                    }

                    if (i == 0) {
                        block = getBlockId(blockpos.getSide(this.leftDir));

                        if (block != OBSIDIAN) {
                            break loop;
                        }
                    } else if (i == this.width - 1) {
                        block = getBlockId(blockpos.getSide(this.rightDir));

                        if (block != OBSIDIAN) {
                            break loop;
                        }
                    }
                }
            }

            for (int i = 0; i < this.width; ++i) {
                if (getBlockId(this.bottomLeft.getSide(this.rightDir, i).up(this.height)) != OBSIDIAN) {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            } else {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        private int getBlockId(Vector3 pos) {
            return this.level.getBlockIdAt(pos.getFloorX(), pos.getFloorY(), pos.getFloorZ());
        }

        protected boolean isEmptyBlock(int id) {
            return force || id == AIR || id == FIRE || id == NETHER_PORTAL;
        }

        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks() {
            for (int i = 0; i < this.width; ++i) {
                Vector3 blockpos = this.bottomLeft.getSide(this.rightDir, i);

                for (int j = 0; j < this.height; ++j) {
                    this.level.setBlock(blockpos.up(j), new BlockNetherPortal(this.axis == Axis.X ? 1 : this.axis == Axis.Z ? 2 : 0));
                }
            }
        }
    }
}
