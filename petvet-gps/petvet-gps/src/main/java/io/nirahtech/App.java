package io.nirahtech;

import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnknownHostException
    {
        final ESP32 esp32 = ESP32.newInstance();
        esp32.run();
    }
}
