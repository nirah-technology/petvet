package io.nirahtech.petvet.esp.brokers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.nirahtech.petvet.esp.messages.Message;
import io.nirahtech.petvet.esp.messages.MessageType;

public final class UDPMessageBroker implements MessageBroker {
    private static final Map<String, MessageBroker> brokers = new HashMap<>();

    public static MessageBroker getOrCreate(final NetworkInterface networkInterface, final InetAddress groupAddress,
            final int port) {
        final String key = String.format("%s(%s:%s)", networkInterface.getName(), groupAddress.toString(), port);
        if (!brokers.containsKey(key)) {
            brokers.put(key, new UDPMessageBroker(networkInterface, groupAddress, port));
        }
        return brokers.get(key);
    }

    private final Map<MessageType, Consumer<Message>> messagesHandlers = new HashMap<>();

    private final InetAddress groupAddress;
    private final int port;
    private final NetworkInterface networkInterface;

    private final MulticastSocket multicastSocket;

    private int timeoutInMilliseconds = 50;

    private UDPMessageBroker(final NetworkInterface networkInterface, final InetAddress groupAddress, final int port) {
        this.networkInterface = networkInterface;
        this.groupAddress = groupAddress;
        this.port = port;
        try {
            this.multicastSocket = new MulticastSocket(this.port);
            this.multicastSocket.joinGroup(new InetSocketAddress(this.groupAddress, this.port), this.networkInterface);
            this.multicastSocket.setSoTimeout(this.timeoutInMilliseconds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setTimeoutInMilliseconds(int timeoutInMilliseconds) {
        this.timeoutInMilliseconds = timeoutInMilliseconds;
        try {
            this.multicastSocket.setSoTimeout(this.timeoutInMilliseconds);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(final Message message) throws IOException {
        final InetSocketAddress group = new InetSocketAddress(this.groupAddress, this.port);
        try (MulticastSocket multicastSocketToSendMessage = new MulticastSocket(this.port)) {
            multicastSocketToSendMessage.joinGroup(group, this.networkInterface);
            final String rowMessage = message.toString();
            final byte[] messageAsBytes = rowMessage.getBytes(StandardCharsets.UTF_8);
            final DatagramPacket datagramPacket = new DatagramPacket(
                    messageAsBytes,
                    0,
                    messageAsBytes.length,
                    group);
                    multicastSocketToSendMessage.send(datagramPacket);
        }
    }

    @Override
    public final void subscribe(final MessageType type, Consumer<Message> handler) {
        this.messagesHandlers.put(type, handler);
    }

    @Override
    public Optional<Message> receive() throws IOException {
        final AtomicReference<Message> receivedMessage = new AtomicReference<>(null);

        final byte[] buffer = new byte[1024];
        final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        boolean hasDataRetrieve = false;
        try {
            multicastSocket.receive(packet);
            hasDataRetrieve = true;
        } catch (SocketTimeoutException e) {

        }
        if (hasDataRetrieve) {
            final String data = new String(packet.getData(), StandardCharsets.UTF_8);
            try {
                final MessageType messageType = MessageType.valueOf(data.split(":", 2)[0]);
                if (Objects.nonNull(messageType)) {
                    final Optional<? extends Message> parsedMessage = messageType.parse(data);
                    parsedMessage.ifPresent(message -> {
                        final MessageType type = message.getType();
                        final Consumer<Message> handler = this.messagesHandlers.get(type);
                        if (Objects.nonNull(handler)) {
                            handler.accept(message);
                        }
                    });
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Optional.ofNullable(receivedMessage.get());
    }

    public void close() {
        brokers.remove(
                String.format("%s(%s:%s)", this.networkInterface.getName(), this.groupAddress.toString(), this.port));
    }
}
