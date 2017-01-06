package cn.nukkit.event.entity;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityVehicle;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;

public class VehicleExitEvent extends EntityEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlers() {
		return handlers;
	}
	
	private Entity rider;
	private EntityVehicle vehicle;
	
	public VehicleExitEvent(Entity rider, EntityVehicle vehicle)
	{
		this.rider = rider;
		this.vehicle = vehicle;
	}
	
	public Entity getRider()
	{
		return this.rider;
	}
	
	public EntityVehicle getVehicle()
	{
		return this.vehicle;
	}
}