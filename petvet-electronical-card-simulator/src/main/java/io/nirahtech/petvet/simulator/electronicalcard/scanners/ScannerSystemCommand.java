package io.nirahtech.petvet.simulator.electronicalcard.scanners;

import java.io.IOException;
import java.util.Set;

public interface ScannerSystemCommand {
    public Set<Device> execute() throws IOException;
}
