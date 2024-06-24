package io.nirahtech.petvet.geopulsetracker;

import java.util.HashSet;
import java.util.Set;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.ui.Window;

public class App {
    public static void main(String[] args) {

        Set<ESP32> esp32s = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            esp32s.add(ESP32.generate());
        }

        Window window = new Window();
        window.setVisible(true);
    }
}