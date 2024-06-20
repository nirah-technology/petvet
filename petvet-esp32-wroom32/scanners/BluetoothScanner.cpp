#include "BluetoothScanner.h"

BluetoothScanner::BluetoothScanner() {
    // Initialisation de la bibliothèque Bluetooth de l'ESP32
    BLEDevice::init("");
}

Microship* BluetoothScanner::scan(int& totalScannedMicroships) {
    // Scanner les périphériques Bluetooth disponibles
    BLEScan* pBLEScan = BLEDevice::getScan();
    pBLEScan->setActiveScan(true);
    BLEScanResults scanResults = pBLEScan->start(5); // Scan pour 5 secondes

    // Récupérer le nombre de périphériques scannés
    totalScannedMicroships = scanResults.getCount();

    // Allouer dynamiquement le tableau des appareils scannés
    this->scannedMicroships = new Microship[totalScannedMicroships];

    // Remplir le tableau avec les appareils scannés
    int index = 0;
    for (int i = 0; i < totalScannedMicroships; i++) {
        BLEAdvertisedDevice device = scanResults.getDevice(i);
        String address = device.getAddress().toString();
        String name = device.getName();
        int rssi = device.getRSSI();
        this->scannedMicroships[i] = Microship(macAddress, ssid, frequency, signalLevel);

        // Supposons que vous avez un constructeur Microship(MacAddress, String, int, float)
        // à adapter selon les informations fournies par l'API Bluetooth de l'ESP32
        // Exemple : MacAddress pourrait être remplacé par BLEAddress ou une conversion appropriée
        // Ensuite, vous pouvez créer un objet Microship pour chaque appareil scanné.
        // this->scannedMicroships[index++] = Microship(macAddress, name, frequency, rssi);
    }

    // Retourner le tableau des appareils scannés
    return this->scannedMicroships;
}
