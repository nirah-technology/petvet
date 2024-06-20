#include "WiFiScanner.h"

WiFiScanner::WiFiScanner() {
    // Configure le wifi pour fonctionne en mode station.
    // Il sera capable de se connecter à d'autres réseaux.
    WiFi.mode(WIFI_STA);

    // On se deconnecte du WiFi. Ca évite les perturbations.
    WiFi.disconnect();
}

Microship* WiFiScanner::scan(int& totalScannedMicroships) {
    // Scanner les réseaux WiFi disponibles
    totalScannedMicroships = WiFi.scanNetworks(); // Utilisation de totalScannedMicroships pour stocker le nombre d'appareils scannés

    // Allouer dynamiquement le tableau des appareils scannés
    this->scannedMicroships = new Microship[totalScannedMicroships];

    // Remplir le tableau avec les appareils scannés
    for (int i = 0; i < totalScannedMicroships; ++i) {
        uint8_t* bssid = WiFi.BSSID(i);
        MacAddress macAddress(bssid);
        String ssid = WiFi.SSID(i);
        int frequency = WiFi.channel(i);
        float signalLevel = WiFi.RSSI(i);

        this->scannedMicroships[i] = Microship(macAddress, ssid, frequency, signalLevel);
    }

    // Retourner le tableau des appareils scannés
    return this->scannedMicroships;
}
