package io.nirahtech.petvet.simulator.electronicalcard.scanners;

import java.io.IOException;
import java.util.stream.Stream;

public final class WifiScanner implements Scanner {

    private final ScannerSystemCommand systemCommand;

    public WifiScanner() {
        if (OperatingSystem.isWindows()) {
            this.systemCommand = new NetShCommand();
        } else if (OperatingSystem.isLinux()) {
            this.systemCommand = new NmCliCommand();
        } else {
            this.systemCommand = new IwListScanCommand();
        }
    }

    @Override
    public Stream<Device> scan() throws IOException {
        return this.systemCommand.execute().stream();
    }

}
