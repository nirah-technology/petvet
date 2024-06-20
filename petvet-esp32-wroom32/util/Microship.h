#ifndef MICROSHIP_H
#define MICROSHIP_H

#include <Arduino.h>
#include "MacAddress.h"

class Microship {
public:
    MacAddress bssid;
    String ssid;
    int frequency;
    float signalLevel;

    Microship(const MacAddress& bssid, const String& ssid, int frequency, float signalLevel);
    String toString() const;
};

#endif // MICROSHIP_H
