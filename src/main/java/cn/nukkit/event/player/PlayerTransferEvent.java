package cn.nukkit.event.player;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.Cancellable;

public class PlayerTransferEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

	public PlayerTransferEvent(Player player, string address, int port){
		this.player = player;
		this.address = address;
		this.port = port;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public string getAddress(){
		return this.address;
	}
	
	public void setAddress(string address){
		this.address = address;
	}
	
	public int getPort(){
		return this.port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
    
}
