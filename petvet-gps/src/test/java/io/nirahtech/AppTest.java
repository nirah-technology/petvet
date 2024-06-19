package io.nirahtech;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import io.nirahtech.petvet.esp.scanners.Scanner;
import io.nirahtech.petvet.esp.scanners.WifiScanner;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     * @throws UnknownHostException
     */
    @Test
    public void shouldAnswerWithTrue() throws UnknownHostException
    {
        final byte[] id = {(byte) 192, (byte) 168, (byte) 0, (byte) 0};
        final byte[] mask = {(byte) 255, (byte) 255, (byte) 255, (byte) 0};
        final InetAddress networkId = InetAddress.getByAddress(id);
        final InetAddress subnetMask = InetAddress.getByAddress(mask);
        System.out.println(subnetMask);
    }
}
