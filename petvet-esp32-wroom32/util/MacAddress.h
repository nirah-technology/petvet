#ifndef MACADDRESS_H
#define MACADDRESS_H

#include <Arduino.h>

class MacAddress {
public:
    uint8_t address[6];

    MacAddress(const uint8_t* macAddress);
    String toString() const;
};

#endif
