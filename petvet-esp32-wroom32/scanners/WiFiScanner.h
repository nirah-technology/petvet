#ifndef WIFI_SCANNER_H
#define WIFI_SCANNER_H

#include "Scanner.h"
#include <WiFi.h> // Inclure la bibliothèque WiFi

class WiFiScanner : public Scanner {
public:
    WiFiScanner();

    // Implémentation de la méthode scan()
    Microship* scan(int& totalScannedMicroships) override;

private:
    Microship* scannedMicroships = nullptr; // Pointeur vers le tableau des appareils scannés
};

#endif // WIFI_SCANNER_H
