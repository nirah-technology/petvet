package io.nirahtech;

import java.io.IOException;

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
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        Scanner scanner = new WifiScanner();
        try {
            scanner.scan();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
