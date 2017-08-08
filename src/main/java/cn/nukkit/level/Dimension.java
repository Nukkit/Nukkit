package cn.nukkit.level;

import cn.nukkit.level.generator.End;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.Nether;
import cn.nukkit.level.generator.Normal;

/**
 * Created by CreeperFace on 8.8.2017.
 */
public enum Dimension {
    OVERWORLD(0, "Overworld", "", Normal.class),
    NETHER(1, "Nether", "_nether", Nether.class),
    END(2, "The End", "_end", End.class);

    private final int id;
    private final String name;
    private final String suffix;
    private final Class<? extends Generator> generator;

    Dimension(int id, String name, String suffix, Class<? extends Generator> generator) {
        this.id = id;
        this.name = name;
        this.suffix = suffix;
        this.generator = generator;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Class<? extends Generator> getGenerator() {
        return generator;
    }

    public static Dimension getById(int id) {
        for (Dimension dimension : values()) {
            if (dimension.getId() == id) {
                return dimension;
            }
        }

        throw new IllegalArgumentException("Invalid dimension id " + id);
    }
}
