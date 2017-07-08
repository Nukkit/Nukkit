package cn.nukkit.event.vehicle;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;

public class EntityExitVehicleEvent extends VehicleEvent implements Cancellable {
    
    private final Entity riding;
    
    public EntityExitVehicleEvent(Entity riding, EntityVehicle vehicle) {
        super(vehicle);
        this.riding = riding;
    }
    
    public Entity getEntity(){
        return riding;
    }
    
    public boolean isPlayer(){
        return riding instanceof Player;
    }

}
