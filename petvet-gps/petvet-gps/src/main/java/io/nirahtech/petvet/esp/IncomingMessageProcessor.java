package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import io.nirahtech.petvet.esp.messages.Message;
import io.nirahtech.petvet.esp.messages.MessageType;

public final class IncomingMessageProcessor implements Runnable {

    private final NetworkInterface networkInterface;
    private final InetAddress groupAddress;
    private final int port;
    private final Map<MessageType, Consumer<Message>> messagesHandlers;

    public IncomingMessageProcessor(NetworkInterface networkInterface, InetAddress groupAddress, int port,
            Map<MessageType, Consumer<Message>> messagesHandlers) {
        this.networkInterface = networkInterface;
        this.groupAddress = groupAddress;
        this.port = port;
        this.messagesHandlers = messagesHandlers;
    }

    @Override
    public void run() {
        try (MulticastSocket multicastSocket = new MulticastSocket(this.port)) {
                multicastSocket.joinGroup(new InetSocketAddress(this.groupAddress, this.port), this.networkInterface);
                while (true) {
                    final byte[] buffer = new byte[1024];
                    final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    multicastSocket.receive(packet);
                    final String receivedMessage = new String(packet.getData(), StandardCharsets.UTF_8);
                    final Optional<Message> parsedMessage = Message.parse(receivedMessage);
                    parsedMessage.ifPresent(message -> {
                        final MessageType type = message.getType();
                        if (Objects.nonNull(type)) {
                            final Optional<? extends Message> realMessageImplementation = type.parse(receivedMessage);
                            if (realMessageImplementation.isPresent()) {
                                final Consumer<Message> handler = this.messagesHandlers.get(type);
                                if (Objects.nonNull(handler)) {
                                    handler.accept(realMessageImplementation.get());
                                }
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
}
