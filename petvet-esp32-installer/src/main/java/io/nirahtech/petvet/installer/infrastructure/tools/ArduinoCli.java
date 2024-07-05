package io.nirahtech.petvet.installer.infrastructure.tools;

import java.util.Objects;

// https://arduino.github.io/arduino-cli/1.0/installation/
// https://arduino.github.io/arduino-cli/1.0/getting-started/
public final class ArduinoCli {

    private static ArduinoCli instance = null;

    /**
     * @return the instance
     */
    public static ArduinoCli getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ArduinoCli();
        }
        return instance;
    }
    
    public ArduinoCli() {

    }

    public void createNewSketch() {

    }

    public void updateSketch(final String newSourceCode) {

    }

    public void installCoreForESP32() {

    }

    public void add3rdPartsCore() {

    }

    public void compile() {

    }

    public void upload() {

    }
}
