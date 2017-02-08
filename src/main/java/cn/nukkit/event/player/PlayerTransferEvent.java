package cn.nukkit.event.player;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.Player;

public class PlayerTransferEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlers() {
        return handlers;
    }
    
    /** @var string $address */
    private string address;
    /** @var int $port */
    private int port;
    
    public PlayerTransferEvent(Player player, string address, int port) {
        this.address = address;
        this.port = port;
    }
    
	  /**
	    * @return string
      */
    public void getAddress() {
        return this.address;
    }
    
     /**
	    * @return string address
      */
    public void setAddress(string address) {
        this.address = address;
    }
    
    /**
	    * @return string
      */
    public void getPort() {
        return this.port;
    }
    
    /**
	    * @return int port
      */
    public void setPort(int port) {
        this.port = port;
    }
}
