package io.nirahtech.petvet.simulator.electronicalcard.scanners;

import java.io.IOException;
import java.util.stream.Stream;

public interface Scanner {
    Stream<Device> scan() throws IOException;
}
