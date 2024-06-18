package io.nirahtech.petvet.esp.scanners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import io.nirahtech.petvet.esp.MacAddress;

public final class NmCliCommand implements ScannerSystemCommand {
    private static final String DETECT_OTHERS_WIFI_NETWORKS_COMMAND = "nmcli -t -f BSSID,SSID,SIGNAL device wifi list";
    private static final String DETECT_WIFI_CARD_COMMAND = "nmcli -t -f DEVICE device";

    private boolean isWifiSupported() throws IOException {
        boolean isSupported = false;
        final ProcessBuilder processBuilder = new ProcessBuilder(DETECT_WIFI_CARD_COMMAND.split(" "));
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("wireless") || line.toLowerCase().startsWith("wl")) {
                isSupported = true;
                break;
            }
        }

        return isSupported;
    }

    public Set<Device> execute() throws IOException {
        final Set<Device> detectedDevices = new HashSet<>();
        if (!this.isWifiSupported()) {
            throw new IOException("WiFi not supported.");
        }
        final ProcessBuilder processBuilder = new ProcessBuilder(DETECT_OTHERS_WIFI_NETWORKS_COMMAND.split(" "));
        processBuilder.redirectErrorStream(true);
        final Process process = processBuilder.start();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            final String[] wifiInfo = line.split(":");
            if (wifiInfo.length == 3) {
                final String bssid = wifiInfo[0].strip();
                final String ssid = wifiInfo[1].strip();
                final float signalInDBm = -Float.parseFloat(wifiInfo[2].strip());
                final Device device = new Device(MacAddress.of(bssid), ssid, 0, signalInDBm);
                detectedDevices.add(device);
            }
        }

        return detectedDevices;
    }
}
