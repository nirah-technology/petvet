package io.nirahtech;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import io.nirahtech.petvet.messaging.messages.HeartBeatMessage;
import io.nirahtech.petvet.messaging.messages.IsOrchestratorAvailableMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     * @throws UnknownHostException 
     */
    @Test
    public void shouldAnswerWithTrue() throws UnknownHostException
    {
        IsOrchestratorAvailableMessage availability = IsOrchestratorAvailableMessage.create(UUID.randomUUID(), MacAddress.of("AA:AA:AA:AA:AA:AA"), InetAddress.getByName("127.0.0.1"), EmitterMode.ORCHESTRATOR_NODE);
        System.out.println(availability.getSentedAt());
        System.out.println(availability);
        IsOrchestratorAvailableMessage.parse(availability.toString()).ifPresent(m -> {
            System.out.println(m.getSentedAt());
        });

        HeartBeatMessage heartbeat = HeartBeatMessage.create(UUID.randomUUID(), MacAddress.of("AA:AA:AA:AA:AA:AA"), InetAddress.getByName("127.0.0.1"), EmitterMode.ORCHESTRATOR_NODE, 0, 0, 0, null);
        System.out.println(heartbeat.getSentedAt());
        System.out.println(heartbeat);
        HeartBeatMessage.parse(heartbeat.toString()).ifPresent(m -> {
            System.out.println(m);
        });
    }
}
