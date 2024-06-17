package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import io.nirahtech.petvet.esp.messages.Message;
import io.nirahtech.petvet.esp.messages.MessageType;

public final class MessageBroker implements MessageReceiver, MessageSender {
    private static final Map<String, MessageBroker> brokers = new HashMap<>();

    public static MessageBroker getOrCreate(final NetworkInterface networkInterface, final InetAddress groupAddress, final int port) {
        final String key = String.format("%s(%s:%s)", networkInterface.getName(), groupAddress.toString(), port);
        if (!brokers.containsKey(key)) {
            brokers.put(key, new MessageBroker(networkInterface, groupAddress, port));
        }
        return brokers.get(key);
    }


    private final Map<MessageType, Consumer<Message>> messagesHandlers = new HashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final InetAddress groupAddress;
    private final int port;
    private final NetworkInterface networkInterface;


    private MessageBroker(final NetworkInterface networkInterface, final InetAddress groupAddress, final int port) {
        this.networkInterface = networkInterface;
        this.groupAddress = groupAddress;
        this.port = port;
    }

    @Override
    public void send(final Message message) throws IOException {
        final InetSocketAddress group = new InetSocketAddress(this.groupAddress, this.port);
        try (MulticastSocket multicastSocket = new MulticastSocket(this.port)) {
            multicastSocket.joinGroup(group, this.networkInterface);
            final String rowMessage = message.toMap().toString();
            final byte[] messageAsBytes = rowMessage.getBytes(StandardCharsets.UTF_8);
            final DatagramPacket datagramPacket = new DatagramPacket(
                messageAsBytes, 
                0,
                messageAsBytes.length,
                group);
            multicastSocket.send(datagramPacket);
        }
    }

    @Override
    public final void subscribe(final MessageType type, Consumer<Message> handler) {
        this.messagesHandlers.put(type, handler);
    }

    public void listenAsync() {
        executorService.submit(new IncomingMessageProcessor(this.networkInterface, this.groupAddress, this.port, this.messagesHandlers));
    }

    public void close() {
        brokers.remove(String.format("%s(%s:%s)", this.networkInterface.getName(), this.groupAddress.toString(), this.port));
    }

}
