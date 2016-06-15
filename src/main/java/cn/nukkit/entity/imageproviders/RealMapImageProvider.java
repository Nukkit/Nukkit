package cn.nukkit.entity.imageproviders;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.biome.SandyBiome;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.MapInfo;

import java.awt.*;

/**
 * author: boybook
 * Nukkit Project
 */

public class RealMapImageProvider extends MapImageProvider {

    @Override
    protected byte[] generateColors(MapInfo map) {
        Level level = Server.getInstance().getLevelByName(map.levelName);
        byte[] bytes = new byte[map.col * map.row * 4];
        if (level != null) {
            if (map.updateType == 0) {
                int i = 0;
                for (int y = 0; y < map.col; y++) {
                    for (int x = 0; x < map.row; x++) {
                        //TODO: middleChunk? offset? updateType?
                        BlockColor color = level.getMapColorAt(x, y);
                        bytes[i++] = (byte)color.getRed(); // R
                        bytes[i++] = (byte)color.getGreen(); // G
                        bytes[i++] = (byte)color.getBlue(); // B
                        bytes[i++] = (byte)color.getAlpha(); // A
                    }
                }
            }

        }

        return bytes;
    }

}
