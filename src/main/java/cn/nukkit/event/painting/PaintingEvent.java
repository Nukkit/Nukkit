package cn.nukkit.event.painting;

import cn.nukkit.entity.item.EntityPainting;
import cn.nukkit.event.Event;

/**
 * Created by fromgate on 28.06.2016.
 */
public class PaintingEvent extends Event {

    protected EntityPainting painting;

    public EntityPainting getPainting() {
        return painting;
    }
}
