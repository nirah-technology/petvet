#ifndef BLUETOOTH_SCANNER_H
#define BLUETOOTH_SCANNER_H

#include "Scanner.h"
#include <BLEDevice.h>
// Inclure la lib blueotooth de l'ESP32

class BluetoothScanner : public Scanner {
public:
    BluetoothScanner();

    // Implémentation de la méthode scan()
    Microship* scan(int& totalScannedMicroships) override;

private:
    Microship* scannedMicroships = nullptr; // Pointeur vers le tableau des appareils scannés
};

#endif // BLUETOOTH_SCANNER_H
