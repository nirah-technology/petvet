package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.Optional;

public final class MulticastReceiver {

    private static final int DEFAULT_BUFFER_SIZE = 128;

    private final int port;
    private byte[] buffer;

    private MulticastReceiver() { 
        this.buffer = new byte[DEFAULT_BUFFER_SIZE];
    }

    /**
     * @param bufferSize the bufferSize to set
     */
    public void setBufferSize(int bufferSize) {
        this.buffer = new byte[bufferSize];
    }
    
    
    /**
     * Receive 
     * @param timeoutInMilliseconds
     * @return
     */
    public final Optional<String> receive(final int timeoutInMilliseconds) {
        Optional<String> receivedMessage = Optional.empty();
        try {
            this.multicastSocketForReception.setSoTimeout(timeoutInMilliseconds);
            while (true) {
                final DatagramPacket incommingUdpPacket = new DatagramPacket(this.buffer.,
                this.buffer.length);
                try {
                    this.multicastSocketForReception.receive(incommingUdpPacket);
                    final String message = new String(incommingUdpPacket.getData(), 0, incommingUdpPacket.getLength());
                    if (Objects.nonNull(message) && !message.isEmpty()) {
                        receivedMessage = Optional.of(message);
                        break;
                    }
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }
}
