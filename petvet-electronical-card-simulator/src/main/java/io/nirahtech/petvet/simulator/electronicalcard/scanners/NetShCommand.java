package io.nirahtech.petvet.simulator.electronicalcard.scanners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class NetShCommand implements ScannerSystemCommand {

    @Override
    public Set<Device> execute() throws IOException {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show networks mode=Bssid");
        builder.redirectErrorStream(true);
        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        return Set.of();

    }

}
