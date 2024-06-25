package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.UUID;

public interface MicroController {

    void powerOn();
    UUID getId();
    void powerOff();
}
