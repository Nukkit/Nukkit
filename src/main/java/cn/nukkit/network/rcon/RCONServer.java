package cn.nukkit.network.rcon;

import cn.nukkit.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;

/**
 * @author Tee7even
 */
public class RCONServer extends Thread {
    private volatile boolean running;

    private ServerSocketChannel serverChannel;
    private Selector selector;

    private final List<Map.Entry<SocketChannel, RCONPacket>> receiveQueue = new ArrayList<>();
    private final Map<SocketChannel, List<RCONPacket>> sendQueues = new HashMap<>();

    public RCONServer(String address, int port) throws IOException {
        setName("RCON");
        this.running = true;

        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);
        this.serverChannel.socket().bind(new InetSocketAddress(address, port));

        this.selector = SelectorProvider.provider().openSelector();
        this.serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
    }

    public Map.Entry<SocketChannel, RCONPacket> receive() {
        synchronized (this.receiveQueue) {
            if (!this.receiveQueue.isEmpty()) {
                Map.Entry<SocketChannel, RCONPacket> pair = this.receiveQueue.get(0);
                this.receiveQueue.remove(0);
                return pair;
            }

            return null;
        }
    }

    public void send(SocketChannel channel, RCONPacket packet) {
        synchronized (this.sendQueues) {
            List<RCONPacket> queue = sendQueues.get(channel);
            if (queue == null) {
                queue = new ArrayList<>();
                sendQueues.put(channel, queue);
            }

            queue.add(packet);
        }

        this.selector.wakeup();
    }

    public void close() {
        this.running = false;
    }

    public void run() {
        while(this.running) {
            try {
                synchronized (this.sendQueues) {
                    for (SocketChannel channel : this.sendQueues.keySet()) {
                        channel.keyFor(this.selector).interestOps(SelectionKey.OP_WRITE);
                    }
                }

                this.selector.select();

                Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.socket();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(this.selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        this.read(key);
                    } else if (key.isWritable()) {
                        this.write(key);
                    }
                }
            } catch (Exception exception) {
                Server.getInstance().getLogger().logException(exception);
            }
        }

        try {
            this.serverChannel.close();
        } catch (IOException exception) {
            Server.getInstance().getLogger().alert(exception.getMessage());
        }

        this.notify();
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int bytesRead;
        try {
            bytesRead = socketChannel.read(buffer);
        } catch (IOException exception) {
            key.cancel();
            socketChannel.close();
            this.receiveQueue.add(new AbstractMap.SimpleEntry<>(socketChannel, null));
            return;
        }

        if (bytesRead == -1) {
            key.cancel();
            socketChannel.close();
            this.receiveQueue.add(new AbstractMap.SimpleEntry<>(socketChannel, null));
            return;
        }

        buffer.flip();
        synchronized (this.receiveQueue) {
            this.receiveQueue.add(new AbstractMap.SimpleEntry<>(socketChannel, new RCONPacket(buffer)));
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        synchronized (this.sendQueues) {
            List<RCONPacket> queue = this.sendQueues.get(socketChannel);

            ByteBuffer buffer = queue.get(0).toBuffer();
            socketChannel.write(buffer);
            queue.remove(0);

            if (queue.isEmpty()) {
                this.sendQueues.remove(socketChannel);
            }

            key.interestOps(SelectionKey.OP_READ);
        }
    }
}
