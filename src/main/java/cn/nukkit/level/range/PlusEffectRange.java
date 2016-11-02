package cn.nukkit.level.range;

import cn.nukkit.math.BlockVector3;

import java.util.ArrayList;
import java.util.List;

/**
 * Nukkit Project
 */
public class PlusEffectRange extends ListEffectRange {
    public PlusEffectRange(int range, boolean includeAboveBelow) {
        super(createList(range, includeAboveBelow));
    }

    private static List<BlockVector3> createList(int range, boolean includeAboveBelow) {
        List<BlockVector3> list = new ArrayList<>();
        list.add(new BlockVector3(0, 0, 0));
        if (includeAboveBelow) {
            list.add(new BlockVector3(0, 1, 0));
            list.add(new BlockVector3(0, -1, 0));
        }
        for (int i = 1; i <= range; i++) {
            list.add(new BlockVector3(0, 0, i));
            list.add(new BlockVector3(0, 0, -i));
            list.add(new BlockVector3(i, 0, 0));
            list.add(new BlockVector3(-i, 0, 0));
        }
        return list;
    }
}
