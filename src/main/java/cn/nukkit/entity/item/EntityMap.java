package cn.nukkit.entity.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.imageproviders.IMapImageProvider;
import cn.nukkit.entity.imageproviders.RealMapImageProvider;
import cn.nukkit.level.Level;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.ClientboundMapItemDataPacket;
import cn.nukkit.utils.MapInfo;

import java.util.*;

/**
 * author: boybook
 * Nukkit Project
 */

public class EntityMap extends Entity {

    public MapInfo mapInfo;
    public IMapImageProvider imageProvider;

    private java.util.List<Player> sentList = new ArrayList<>();

    public EntityMap(Level level) {
        this(level, new MapInfo(), new RealMapImageProvider());
        mapInfo.mapId = this.getId();
        mapInfo.updateType = 0;
        mapInfo.direction = 0;
        mapInfo.levelName = level.getFolderName();
        mapInfo.x = 0;
        mapInfo.z = 0;
        mapInfo.col = 128;
        mapInfo.row = 128;
        mapInfo.xOffset = 0;
        mapInfo.zOffset = 0;
    }

    public EntityMap(Level level, MapInfo mapInfo, IMapImageProvider mapImageProvider) {
        super(level.getChunk(0, 0),
                new CompoundTag()
                        .putList(new ListTag<DoubleTag>("Pos")
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0)))
                        .putList(new ListTag<DoubleTag>("Motion")
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0))
                                .add(new DoubleTag("", 0)))
                        .putList(new ListTag<FloatTag>("Rotation")
                                .add(new FloatTag("", 0))
                                .add(new FloatTag("", 0))
                        ));

        imageProvider = mapImageProvider;

        this.mapInfo = mapInfo;
    }

    @Override
    public int getNetworkId() {
        return 0;
    }

    @Override
    public void spawnTo(Player player) {
        // This is a server-side only entity
    }

    @Override
    public void despawnFrom(Player player) {
        // This is a server-side only entity
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        int tickDiff = currentTick - this.lastUpdate;

        if (tickDiff <= 0 && !this.justCreated) {
            return true;
        }

        this.lastUpdate = currentTick;

        if (currentTick % 2 != 0) return true;

        // if no image provider, do nothing
        if (imageProvider == null) return true;

        byte[] data = imageProvider.getData(mapInfo, false);

        if (data != null) {
            mapInfo.data = data;
            MapInfo mapInfo = this.mapInfo.clone();

            ClientboundMapItemDataPacket pk = new ClientboundMapItemDataPacket();
            pk.mapInfo = mapInfo;

            this.checkSentList();
            for (Player p: this.getLevel().getPlayers().values()) {
                if (!this.sentList.contains(p)) {
                    this.sentList.add(p);
                    p.dataPacket(pk);
                    this.getServer().getLogger().debug("Send Map data(0) to " + p.getName() + ": " + mapInfo.mapId);
                }
            }
        }
        return true;
    }

    public void checkSentList() {
        for (Player player: new ArrayList<>(this.sentList)) {
            if (!player.isOnline()) this.sentList.remove(player);
        }
    }

    public void addToMapListeners(Player player, long mapId) {
        if (mapId == this.getId()) {
            if (imageProvider == null) return;

            byte[] data = imageProvider.getData(mapInfo, true);
            if (data != null) {
                mapInfo.data = data;
                MapInfo mapInfo = this.mapInfo.clone();

                ClientboundMapItemDataPacket pk = new ClientboundMapItemDataPacket();
                pk.mapInfo = mapInfo;
                this.getServer().getLogger().debug("Send Map data to " + player.getName() + "(0): " + mapInfo.mapId);
                player.dataPacket(pk);
            }
        }
    }

}
