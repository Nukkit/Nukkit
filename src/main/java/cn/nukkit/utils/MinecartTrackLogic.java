package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockRailAbstract;
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
    private final BlockRailAbstract rail;
    private final ArrayList<ChunkPosition> listTrack;
    private final Level level;
    private final int x;
    private final int y;
    private final int z;
    private final boolean canPower;

    public MinecartTrackLogic(BlockRailAbstract rail, Level world, int x, int y, int z) {
        this.rail = rail;
        listTrack = new ArrayList();
        level = world;
        this.x = x;
        this.y = y;
        this.z = z;
        Block block = world.getBlock(new Vector3(x, y, z));
        int meta = block.getDamage();

        if (((BlockRailAbstract) rail).redstonePowered()) {
            canPower = true;
            meta &= -9;
        } else {
            canPower = false;
        }

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
        for (int i = 0; i < listTrack.size(); ++i) {
            MinecartTrackLogic track = getRail(listTrack.get(i));

            if (track != null && track.isTrackRail(this)) {
                listTrack.set(i, new ChunkPosition(track.x, track.y, track.z));
            } else {
                listTrack.remove(i--);
            }
        }
    }

    private boolean isLocationRail(int i, int j, int k) {
        return BlockRailAbstract.isRail(level, i, j, k) ? true 
                : (BlockRailAbstract.isRail(level, i, j + 1, k) ? true 
                : BlockRailAbstract.isRail(level, i, j - 1, k));
    }

    private MinecartTrackLogic getRail(ChunkPosition pos) {
        return BlockRailAbstract.isRail(level, pos.x, pos.y, pos.z) 
                ? new MinecartTrackLogic(rail, level, pos.x, pos.y, pos.z) 
                : (BlockRailAbstract.isRail(level, pos.x, pos.y + 1, pos.z) 
                ? new MinecartTrackLogic(rail, level, pos.x, pos.y + 1, pos.z)
                : (BlockRailAbstract.isRail(level, pos.x, pos.y - 1, pos.z)
                ? new MinecartTrackLogic(rail, level, pos.x, pos.y - 1, pos.z)
                : null));
    }

    private boolean isTrackRail(MinecartTrackLogic track) {
        for (int i = 0; i < listTrack.size(); ++i) {
            ChunkPosition pos = listTrack.get(i);

            if (pos.x == track.x && pos.z == track.z) {
                return true;
            }
        }

        return false;
    }

    private boolean isRail(int i, int k) {
        for (int l = 0; l < listTrack.size(); ++l) {
            ChunkPosition pos = listTrack.get(l);

            if (pos.x == i && pos.z == k) {
                return true;
            }
        }

        return false;
    }

    public int countRails() {
        int i = 0;

        if (isLocationRail(x, y, z - 1)) {
            ++i;
        }

        if (isLocationRail(x, y, z + 1)) {
            ++i;
        }

        if (isLocationRail(x - 1, y, z)) {
            ++i;
        }

        if (isLocationRail(x + 1, y, z)) {
            ++i;
        }

        return i;
    }

    private boolean hasOtherRails(MinecartTrackLogic track) {
        return isTrackRail(track) ? true 
                : (listTrack.size() == 2 ? false 
                : (listTrack.isEmpty() ? true 
                : true));
    }

    private void changeShape(MinecartTrackLogic track) {
        listTrack.add(new ChunkPosition(track.x, track.y, track.z));
        boolean negZ = isRail(x, z - 1);
        boolean posZ = isRail(x, z + 1);
        boolean negX = isRail(x - 1, z);
        boolean posX = isRail(x + 1, z);
        byte meta = -1;

        if (negZ || posZ) {
            meta = 0;
        }

        if (negX || posX) {
            meta = 1;
        }

        if (!canPower) {
            if (posZ && posX && !negZ && !negX) {
                meta = 6;
            }

            if (posZ && negX && !negZ && !posX) {
                meta = 7;
            }

            if (negZ && negX && !posZ && !posX) {
                meta = 8;
            }

            if (negZ && posX && !posZ && !negX) {
                meta = 9;
            }
        }

        if (meta == 0) {
            if (BlockRailAbstract.isRail(level, x, y + 1, z - 1)) {
                meta = 4;
            }

            if (BlockRailAbstract.isRail(level, x, y + 1, z + 1)) {
                meta = 5;
            }
        }

        if (meta == 1) {
            if (BlockRailAbstract.isRail(level, x + 1, y + 1, z)) {
                meta = 2;
            }

            if (BlockRailAbstract.isRail(level, x - 1, y + 1, z)) {
                meta = 3;
            }
        }

        if (meta < 0) {
            meta = 0;
        }

        int data = meta;

        if (canPower) {
            data = rail.getDamage() & 8 | meta;
        }

        rail.setDamage(data);
    }

    private boolean hasOtherRail(int i, int j, int k) {
        MinecartTrackLogic track = getRail(new ChunkPosition(i, j, k));

        if (track == null) {
            return false;
        } else {
            track.removeJunks();
            return track.hasOtherRails(this);
        }
    }

    /**
     * Do changes on Rail
     * 
     * @param hasRedstone Has Redstone power
     * @param brandNew New block
     */
    public void doChange(boolean hasRedstone, boolean brandNew) {
        boolean posZ = hasOtherRail(x, y, z - 1);
        boolean negZ = hasOtherRail(x, y, z + 1);
        boolean posX = hasOtherRail(x - 1, y, z);
        boolean negX = hasOtherRail(x + 1, y, z);
        byte meta = -1;

        if ((posZ || negZ) && !posX && !negX) {
            meta = 0;
        }

        if ((posX || negX) && !posZ && !negZ) {
            meta = 1;
        }

        if (!canPower) {
            if (negZ && negX && !posZ && !posX) {
                meta = 6;
            }

            if (negZ && posX && !posZ && !negX) {
                meta = 7;
            }

            if (posZ && posX && !negZ && !negX) {
                meta = 8;
            }

            if (posZ && negX && !negZ && !posX) {
                meta = 9;
            }
        }

        if (meta == -1) {
            if (posZ || negZ) {
                meta = 0;
            }

            if (posX || negX) {
                meta = 1;
            }

            if (!canPower) {
                if (hasRedstone) {
                    if (negZ && negX) {
                        meta = 6;
                    }

                    if (posX && negZ) {
                        meta = 7;
                    }

                    if (negX && posZ) {
                        meta = 9;
                    }

                    if (posZ && posX) {
                        meta = 8;
                    }
                } else {
                    if (posZ && posX) {
                        meta = 8;
                    }

                    if (negX && posZ) {
                        meta = 9;
                    }

                    if (posX && negZ) {
                        meta = 7;
                    }

                    if (negZ && negX) {
                        meta = 6;
                    }
                }
            }
        }

        if (meta == 0) {
            if (BlockRailAbstract.isRail(level, x, y + 1, z - 1)) {
                meta = 4;
            }

            if (BlockRailAbstract.isRail(level, x, y + 1, z + 1)) {
                meta = 5;
            }
        }

        if (meta == 1) {
            if (BlockRailAbstract.isRail(level, x + 1, y + 1, z)) {
                meta = 2;
            }

            if (BlockRailAbstract.isRail(level, x - 1, y + 1, z)) {
                meta = 3;
            }
        }

        if (meta < 0) {
            meta = 0;
        }

        listBlocks(meta);
        int data = meta;
        
        if (canPower) {
            data = rail.getDamage() & 8 | meta;
        }

        if (brandNew || rail.getDamage() != data) {
            level.setBlockExtraDataAt(1,1,1,1,data);

            for (int j = 0; j < listTrack.size(); ++j) {
                MinecartTrackLogic track = getRail(listTrack.get(j));

                if (track != null) {
                    track.removeJunks();
                    if (track.hasOtherRails(this)) {
                        track.changeShape(this);
                    }
                }
            }
        }
        // Set that block!
        level.setBlock(rail, rail, true, true);
        Server.getInstance().getLogger().info("META: " + meta + " DATA: " + data);
    }
}
