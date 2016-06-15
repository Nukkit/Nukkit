package cn.nukkit.entity.imageproviders;

import cn.nukkit.utils.MapInfo;

import java.awt.*;

/**
 * author: boybook
 * Nukkit Project
 */

public class ColorMapImageProvider extends MapImageProvider {

    private Color color;

    public ColorMapImageProvider(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected byte[] generateColors(MapInfo map) {
        byte[] bytes = new byte[map.col * map.row * 4];
        System.out.println(bytes.length);

        int i = 0;
        for (int y = 0; y < map.col; y++) {
            for (int x = 0; x < map.row; x++) {
                bytes[i++] = (byte)color.getRed(); // R
                bytes[i++] = (byte)color.getGreen(); // G
                bytes[i++] = (byte)color.getBlue(); // B
                bytes[i++] = (byte)color.getAlpha(); // A
            }
        }

        return bytes;
    }

}
