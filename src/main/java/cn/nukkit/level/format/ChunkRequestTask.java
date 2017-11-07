package cn.nukkit.level.format;

import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.generic.BaseChunk;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.BinaryStream;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author CreeperFace
 */
public class ChunkRequestTask extends AsyncTask {

    private byte[] data;
    private Level level;
    private BaseChunk chunk;
    private BatchPacket finalData;
    private BatchPacket finalData11;

    public ChunkRequestTask(Level level, BaseChunk chunk) {
        this.level = level;
        this.chunk = chunk.clone();
    }

    @Override
    public void onRun() {
        //BaseChunk chunk = cn.nukkit.level.format.anvil.Chunk.fromFastBinary(this.data);

        byte[] blockEntities = new byte[0];
        byte[] blockEntities11 = new byte[0];

        if (!chunk.getBlockEntities().isEmpty()) {
            List<CompoundTag> tagList = new ArrayList<>();
            List<CompoundTag> tagList11 = new ArrayList<>();

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (blockEntity instanceof BlockEntitySpawnable) {
                    tagList.add(((BlockEntitySpawnable) blockEntity).getSpawnCompound());
                    tagList11.add(((BlockEntitySpawnable) blockEntity).getSpawnCompound11());
                }
            }

            try {
                blockEntities = NBTIO.write(tagList, ByteOrder.LITTLE_ENDIAN, true);
                blockEntities11 = NBTIO.write(tagList11, ByteOrder.LITTLE_ENDIAN, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Integer, Integer> extra = chunk.getBlockExtraDataArray();
        BinaryStream extraData;
        if (!extra.isEmpty()) {
            extraData = new BinaryStream();
            extraData.putVarInt(extra.size());
            for (Map.Entry<Integer, Integer> entry : extra.entrySet()) {
                extraData.putVarInt(entry.getKey());
                extraData.putLShort(entry.getValue());
            }
        } else {
            extraData = null;
        }

        BinaryStream stream = new BinaryStream();
        BinaryStream stream11 = new BinaryStream();

        int count = 0;
        cn.nukkit.level.format.ChunkSection[] sections = chunk.getSections();
        for (int i = sections.length - 1; i >= 0; i--) {
            if (!sections[i].isEmpty()) {
                count = i + 1;
                break;
            }
        }
        stream.putByte((byte) count);
        stream11.putByte((byte) count);

        for (int i = 0; i < count; i++) {
            stream.putByte((byte) 0);
            stream11.putByte((byte) 0);
            stream.put(sections[i].getBytes());
            stream11.put(sections[i].getBytes11());
        }
        for (int height : chunk.getHeightMapArray()) {
            stream.putByte((byte) height);
            stream11.putByte((byte) height);
        }
        stream.put(new byte[256]);
        stream.put(chunk.getBiomeIdArray());
        stream.putByte((byte) 0);
        stream11.put(new byte[256]);
        stream11.put(chunk.getBiomeIdArray());
        stream11.putByte((byte) 0);
        if (extraData != null) {
            stream.put(extraData.getBuffer());
            stream11.put(extraData.getBuffer());
        } else {
            stream.putVarInt(0);
            stream11.putVarInt(0);
        }
        stream.put(blockEntities);
        stream11.put(blockEntities11);

        int x = chunk.getX();
        int z = chunk.getZ();

        this.finalData = Level.getChunkCacheFromData(x, z, stream.getBuffer(), ProtocolInfo.CURRENT_PROTOCOL);
        this.finalData11 = Level.getChunkCacheFromData(x, z, stream11.getBuffer(), 113);
    }

    @Override
    public void onCompletion(Server server) {
        Level lvl = server.getLevelByName(this.level.getFolderName());

        if (lvl == null || lvl.getId() != this.level.getId()) {
            return;
        }

        this.level.chunkRequestCallback(chunk.getX(), chunk.getZ(), finalData, finalData11);
    }
}
