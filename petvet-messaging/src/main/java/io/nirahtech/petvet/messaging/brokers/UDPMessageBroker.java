package io.nirahtech.petvet.messaging.brokers;

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

import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.messages.MessageType;

public final class UDPMessageBroker implements MessageBroker {

    public static MessageBroker newInstance() {
        return new UDPMessageBroker();
    }

    private final Map<MessageType, Consumer<Message>> messagesHandlers = new HashMap<>();

    private InetAddress groupAddress;
    private int port;
    private NetworkInterface networkInterface;

    private MulticastSocket multicastSocket;

    private int timeoutInMilliseconds = 50;

    private UDPMessageBroker() {

    }

    @Override
    public void connect(NetworkInterface networkInterface, InetAddress groupAddress, int port) throws IOException {
        if (this.isConnected()) {
            this.disconnect();
        }
        this.networkInterface = networkInterface;
        this.groupAddress = groupAddress;
        this.port = port;
        this.multicastSocket = new MulticastSocket(this.port);
        this.multicastSocket.joinGroup(new InetSocketAddress(this.groupAddress, this.port), this.networkInterface);
        this.multicastSocket.setSoTimeout(this.timeoutInMilliseconds);
    }

    @Override
    public boolean isConnected() {
        return Objects.nonNull(this.multicastSocket);
    }

    @Override
    public void disconnect() {
        if (Objects.nonNull(this.multicastSocket)) {
            this.multicastSocket.close();
            this.multicastSocket = null;
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
            this.multicastSocket.receive(packet);
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
                        receivedMessage.set(message);
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

    @Override
    public InetAddress getInetAddress() {
        return this.groupAddress;
    }



    @Override
    public int getPort() {
        return this.port;
    }
}
