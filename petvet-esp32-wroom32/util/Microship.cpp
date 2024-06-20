#include "Microship.h"

Microship::Microship(const MacAddress& bssid, const String& ssid, int frequency, float signalLevel)
    : bssid(bssid), ssid(ssid), frequency(frequency), signalLevel(signalLevel) {}

String Microship::toString() const {
    char macAsString[18];
    sprintf(macAsString, "%02X:%02X:%02X:%02X:%02X:%02X", 
            address[0], address[1], address[2], 
            address[3], address[4], address[5]);
    return String(macAsString);
}