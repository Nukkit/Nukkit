package cn.nukkit.utils;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRail;
import cn.nukkit.level.ChunkPosition;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;

/**
 * Author: Adam Matthew
 * <p>
 * Nukkit Project: Minecart Logical
 */
public class MinecartTrackLogic {

    /**
     * Rail connecting parts
     * ----
     * Complex? I don't think so
     * Try to think something else
     */
    private final BlockRail rail;
    private final ArrayList listTrack;
    private final Level level;
    private final int x;
    private final int y;
    private final int z;
    private final boolean canPower;

    public MinecartTrackLogic(BlockRail rail, Level world, int x, int y, int z) {
        this.rail = rail;
        this.listTrack = new ArrayList();
        this.level = world;
        this.x = x;
        this.y = y;
        this.z = z;
        Block block = world.getBlock(new Vector3(x, y, z));
        int meta = block.getDamage();

//        if (((BlockRail) rail).redstonePowered()) {
//            canPower = true;
//            meta &= -9;
//        } else {
            canPower = false;
//        }

        listBlocks(meta);
    }

    private void listBlocks(int meta) {
        listTrack.clear();
        switch (meta) {
            case 0:
                listTrack.add(new ChunkPosition(x, y, z - 1));
                listTrack.add(new ChunkPosition(x, y, z + 1));
                break;
            case 1:
                listTrack.add(new ChunkPosition(x - 1, y, z));
                listTrack.add(new ChunkPosition(x + 1, y, z));
                break;
            case 2:
                listTrack.add(new ChunkPosition(x - 1, y, z));
                listTrack.add(new ChunkPosition(x + 1, y + 1, z));
                break;
            case 3:
                listTrack.add(new ChunkPosition(x - 1, y + 1, z));
                listTrack.add(new ChunkPosition(x + 1, y, z));
                break;
            case 4:
                listTrack.add(new ChunkPosition(x, y + 1, z - 1));
                listTrack.add(new ChunkPosition(x, y, z + 1));
                break;
            case 5:
                listTrack.add(new ChunkPosition(x, y, z - 1));
                listTrack.add(new ChunkPosition(x, y + 1, z + 1));
                break;
            case 6:
                listTrack.add(new ChunkPosition(x + 1, y, z));
                listTrack.add(new ChunkPosition(x, y, z + 1));
                break;
            case 7:
                listTrack.add(new ChunkPosition(x - 1, y, z));
                listTrack.add(new ChunkPosition(x, y, z + 1));
                break;
            case 8:
                listTrack.add(new ChunkPosition(x - 1, y, z));
                listTrack.add(new ChunkPosition(x, y, z - 1));
                break;
            case 9:
                listTrack.add(new ChunkPosition(x + 1, y, z));
                listTrack.add(new ChunkPosition(x, y, z - 1));
                break;
            default:
                break;
        }
    }

    private void removeJunks() {
        for (int i = 0; i < this.listTrack.size(); ++i) {
            MinecartTrackLogic track = this.isRail((ChunkPosition) this.listTrack.get(i));

            if (track != null && track.isRail(this)) {
                this.listTrack.set(i, new ChunkPosition(track.x, track.y, track.z));
            } else {
                this.listTrack.remove(i--);
            }
        }
    }

    private boolean isRail(int i, int j, int k) {
        return BlockRail.isRail(this.level, i, j, k) ? true : (BlockRail.isRail(this.level, i, j + 1, k) ? true : BlockRail.isRail(this.level, i, j - 1, k));
    }

    private MinecartTrackLogic isRail(ChunkPosition pos) {
        return BlockRail.isRail(this.level, pos.x, pos.y, pos.z) 
                ? new MinecartTrackLogic(this.rail, this.level, pos.x, pos.y, pos.z) 
                : (BlockRail.isRail(this.level, pos.x, pos.y + 1, pos.z) 
                ? new MinecartTrackLogic(this.rail, this.level, pos.x, pos.y + 1, pos.z)
                : (BlockRail.isRail(this.level, pos.x, pos.y - 1, pos.z)
                ? new MinecartTrackLogic(this.rail, this.level, pos.x, pos.y - 1, pos.z)
                : null));
    }

    private boolean isRail(MinecartTrackLogic track) {
        for (int i = 0; i < this.listTrack.size(); ++i) {
            ChunkPosition pos = (ChunkPosition) this.listTrack.get(i);

            if (pos.x == track.x && pos.z == track.z) {
                return true;
            }
        }

        return false;
    }

    private boolean isRail(int i, int k) {
        for (int l = 0; l < this.listTrack.size(); ++l) {
            ChunkPosition pos = (ChunkPosition) this.listTrack.get(l);

            if (pos.x == i && pos.z == k) {
                return true;
            }
        }

        return false;
    }

    protected int countRails() {
        int i = 0;

        if (this.isRail(this.x, this.y, this.z - 1)) {
            ++i;
        }

        if (this.isRail(this.x, this.y, this.z + 1)) {
            ++i;
        }

        if (this.isRail(this.x - 1, this.y, this.z)) {
            ++i;
        }

        if (this.isRail(this.x + 1, this.y, this.z)) {
            ++i;
        }

        return i;
    }

    private boolean unknownVarStatement(MinecartTrackLogic track) {
        return this.isRail(track) ? true : (this.listTrack.size() == 2 ? false : (this.listTrack.isEmpty() ? true : true));
    }

    private void changeShape(MinecartTrackLogic track) {
        this.listTrack.add(new ChunkPosition(track.x, track.y, track.z));
        boolean flag = this.isRail(this.x, this.z - 1);
        boolean flag1 = this.isRail(this.x, this.z + 1);
        boolean flag2 = this.isRail(this.x - 1, this.z);
        boolean flag3 = this.isRail(this.x + 1, this.z);
        byte b0 = -1;

        if (flag || flag1) {
            b0 = 0;
        }

        if (flag2 || flag3) {
            b0 = 1;
        }

        if (!this.canPower) {
            if (flag1 && flag3 && !flag && !flag2) {
                b0 = 6;
            }

            if (flag1 && flag2 && !flag && !flag3) {
                b0 = 7;
            }

            if (flag && flag2 && !flag1 && !flag3) {
                b0 = 8;
            }

            if (flag && flag3 && !flag1 && !flag2) {
                b0 = 9;
            }
        }

        if (b0 == 0) {
            if (BlockRail.isRail(this.level, this.x, this.y + 1, this.z - 1)) {
                b0 = 4;
            }

            if (BlockRail.isRail(this.level, this.x, this.y + 1, this.z + 1)) {
                b0 = 5;
            }
        }

        if (b0 == 1) {
            if (BlockRail.isRail(this.level, this.x + 1, this.y + 1, this.z)) {
                b0 = 2;
            }

            if (BlockRail.isRail(this.level, this.x - 1, this.y + 1, this.z)) {
                b0 = 3;
            }
        }

        if (b0 < 0) {
            b0 = 0;
        }

        int i = b0;

        if (this.canPower) {
            i = this.level.getBlock(new Vector3(this.x, this.y, this.z)).getDamage() & 8 | b0;
        }

        this.level.getBlock(new Vector3(this.x, this.y, this.z)).setDamage(i);
    }

    private boolean isPRail(int i, int j, int k) {
        MinecartTrackLogic track = this.isRail(new ChunkPosition(i, j, k));

        if (track == null) {
            return false;
        } else {
            track.countRails();
            return track.unknownVarStatement(this);
        }
    }

    public void doChange(boolean flag, boolean flag1) {
        boolean flag2 = this.isPRail(this.x, this.y, this.z - 1);
        boolean flag3 = this.isPRail(this.x, this.y, this.z + 1);
        boolean flag4 = this.isPRail(this.x - 1, this.y, this.z);
        boolean flag5 = this.isPRail(this.x + 1, this.y, this.z);
        byte b0 = -1;

        if ((flag2 || flag3) && !flag4 && !flag5) {
            b0 = 0;
        }

        if ((flag4 || flag5) && !flag2 && !flag3) {
            b0 = 1;
        }

        if (!this.canPower) {
            if (flag3 && flag5 && !flag2 && !flag4) {
                b0 = 6;
            }

            if (flag3 && flag4 && !flag2 && !flag5) {
                b0 = 7;
            }

            if (flag2 && flag4 && !flag3 && !flag5) {
                b0 = 8;
            }

            if (flag2 && flag5 && !flag3 && !flag4) {
                b0 = 9;
            }
        }

        if (b0 == -1) {
            if (flag2 || flag3) {
                b0 = 0;
            }

            if (flag4 || flag5) {
                b0 = 1;
            }

            if (!this.canPower) {
                if (flag) {
                    if (flag3 && flag5) {
                        b0 = 6;
                    }

                    if (flag4 && flag3) {
                        b0 = 7;
                    }

                    if (flag5 && flag2) {
                        b0 = 9;
                    }

                    if (flag2 && flag4) {
                        b0 = 8;
                    }
                } else {
                    if (flag2 && flag4) {
                        b0 = 8;
                    }

                    if (flag5 && flag2) {
                        b0 = 9;
                    }

                    if (flag4 && flag3) {
                        b0 = 7;
                    }

                    if (flag3 && flag5) {
                        b0 = 6;
                    }
                }
            }
        }

        if (b0 == 0) {
            if (BlockRail.isRail(this.level, this.x, this.y + 1, this.z - 1)) {
                b0 = 4;
            }

            if (BlockRail.isRail(this.level, this.x, this.y + 1, this.z + 1)) {
                b0 = 5;
            }
        }

        if (b0 == 1) {
            if (BlockRail.isRail(this.level, this.x + 1, this.y + 1, this.z)) {
                b0 = 2;
            }

            if (BlockRail.isRail(this.level, this.x - 1, this.y + 1, this.z)) {
                b0 = 3;
            }
        }

        if (b0 < 0) {
            b0 = 0;
        }

        this.listBlocks(b0);
        int i = b0;

        if (this.canPower) {
            i = this.level.getBlock(new Vector3(this.x, this.y, this.z)).getDamage() & 8 | b0;
        }

        if (flag1 || this.level.getBlock(new Vector3(this.x, this.y, this.z)).getDamage() != i) {
            this.level.getBlock(new Vector3(this.x, this.y, this.z)).setDamage(i);

            for (int j = 0; j < this.listTrack.size(); ++j) {
                MinecartTrackLogic track = this.isRail((ChunkPosition) this.listTrack.get(j));

                if (track != null) {
                    track.countRails();
                    if (track.unknownVarStatement(this)) {
                        track.changeShape(this);
                    }
                }
            }
        }
    }
}
