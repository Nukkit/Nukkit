package cn.nukkit.item.randomitem;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Snake1999 on 2016/1/15.
 * Package cn.nukkit.item.randomitem in project nukkit.
 */
public final class RandomItem {
    private static final Map<Selector, Float> selectors = new ConcurrentHashMap<>(8, 0.9f, 1);

    public static final Selector ROOT = new Selector(null);

    public static Selector putSelector(Selector selector) {
        return putSelector(selector, 1);
    }

    public static Selector putSelector(Selector selector, float chance) {
        if (selector.getParent() == null) selector.setParent(ROOT);
        selectors.put(selector, chance);
        return selector;
    }

    static Object selectFrom(Selector selector) {
        Objects.requireNonNull(selector);
        Map<Selector, Float> child = new ConcurrentHashMap<>(8, 0.9f, 1);
        selectors.forEach((s, f) -> {
            if (s.getParent() == selector) child.put(s, f);
        });
        if (child.size() == 0) return selector.select();
        return selectFrom(Selector.selectRandom(child));
    }

}
