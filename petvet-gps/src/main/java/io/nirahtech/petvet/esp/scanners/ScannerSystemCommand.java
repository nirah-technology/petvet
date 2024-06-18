package io.nirahtech.petvet.esp.scanners;

import java.io.IOException;
import java.util.Set;

public interface ScannerSystemCommand {
    public Set<Device> execute() throws IOException;
}
