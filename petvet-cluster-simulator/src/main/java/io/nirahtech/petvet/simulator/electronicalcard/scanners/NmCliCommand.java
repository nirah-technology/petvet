package io.nirahtech.petvet.simulator.electronicalcard.scanners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.Signal;

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
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        Pattern pattern = Pattern.compile(":");
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\\\\:", ":");
            String[] wifiInfo = pattern.split(line, -1); // Utilisation de -1 pour conserver les champs vides
            if (wifiInfo.length == 8) {
                // Les trois premiers champs doivent être BSSID, SSID et signalInDBm
                // respectivement
                String bssid = line.substring(0, 17).strip();
                String ssid = wifiInfo[6].strip();
                try {
                    final float percentage = Float.parseFloat(wifiInfo[7].strip());
                    final float signalInDBm = Signal.percentageToDbm(percentage);
                    // Création de l'objet Device
                    Device device = new Device(MacAddress.of(bssid), ssid, 0, signalInDBm);
                    detectedDevices.add(device);
                } catch (NumberFormatException e) {

                }
            }
        }
        return detectedDevices;
    }
}
