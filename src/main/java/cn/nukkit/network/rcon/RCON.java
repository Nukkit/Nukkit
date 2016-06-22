package cn.nukkit.network.rcon;

import cn.nukkit.Server;
import cn.nukkit.command.RemoteConsoleCommandSender;
import cn.nukkit.event.server.RemoteServerCommandEvent;
import cn.nukkit.utils.TextFormat;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://developer.valvesoftware.com/wiki/Source_RCON_Protocol
 * @author Tee7even
 */
public class RCON {
    private static final int SERVERDATA_AUTH = 3;
    private static final int SERVERDATA_AUTH_RESPONSE = 2;
    private static final int SERVERDATA_EXECCOMMAND = 2;
    private static final int SERVERDATA_RESPONSE_VALUE = 0;

    private Server server;
    private String password;
    private RCONServer serverThread;
    private Set<SocketChannel> rconSessions = new HashSet<>();

    public RCON(Server server, String password, String address, int port) {
        if (password.isEmpty()) {
            server.getLogger().critical("Failed to start RCON: password is empty");
            return;
        }

        this.server = server;
        this.password = password;

        try {
            this.serverThread = new RCONServer(address, port);
            this.serverThread.start();
        } catch (IOException exception) {
            this.server.getLogger().critical("Failed to start RCON: " + exception.getMessage());
            return;
        }

        this.server.getLogger().info("RCON is running on " + address + ":" + port);
    }

    public void check() {
        if (this.serverThread == null) {
            return;
        } else if (!this.serverThread.isAlive()) {
            return;
        }

        Map.Entry<SocketChannel, RCONPacket> pair;
        while ((pair = serverThread.receive()) != null) {
            if (pair.getValue() == null) {
                if (rconSessions.contains(pair.getKey())) {
                    rconSessions.remove(pair.getKey());
                }
                continue;
            }

            switch (pair.getValue().getType()) {
                case SERVERDATA_AUTH:
                    byte[] payload = new byte[1];
                    payload[0] = 0;

                    if (new String(pair.getValue().getPayload(), Charset.forName("UTF-8")).equals(this.password)) {
                        this.rconSessions.add(pair.getKey());
                        this.serverThread.send(pair.getKey(), new RCONPacket(pair.getValue().getId(), SERVERDATA_AUTH_RESPONSE, payload));
                        continue;
                    }

                    this.serverThread.send(pair.getKey(), new RCONPacket(-1, SERVERDATA_AUTH_RESPONSE, payload));
                    break;
                case SERVERDATA_EXECCOMMAND:
                    if (!this.rconSessions.contains(pair.getKey())) {
                        continue;
                    }

                    String command = new String(pair.getValue().getPayload(), Charset.forName("UTF-8")).trim();
                    RemoteConsoleCommandSender sender = new RemoteConsoleCommandSender();
                    RemoteServerCommandEvent event = new RemoteServerCommandEvent(sender, command);
                    this.server.getPluginManager().callEvent(event);

                    if(!event.isCancelled()) {
                        this.server.dispatchCommand(sender, command);
                    }

                    this.serverThread.send(pair.getKey(), new RCONPacket(pair.getValue().getId(), SERVERDATA_RESPONSE_VALUE, TextFormat.clean(sender.getMessages()).getBytes()));
                    break;
            }
        }
    }

    public void close() {
        serverThread.close();
        try {
            serverThread.wait(5000);
        } catch (InterruptedException exception) {
            //
        }
    }
}
