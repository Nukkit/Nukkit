package cn.nukkit.event.player;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.Player;

public class PlayerTransferEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlers() {
        return handlers;
    }
    
    /** @var String $address */
    private String address;
    /** @var int $port */
    private int port;
    
    public PlayerTransferEvent(Player player, String address, int port) {
        this.address = address;
        this.port = port;
    }
    
    /**
      * @return String
      */
    public void getAddress() {
        return this.address;
    }
    
    /**
      * @return String address
      */
    public void setAddress(String address) {
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
