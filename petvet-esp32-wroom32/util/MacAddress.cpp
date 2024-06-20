#include "MacAddress.h"
#include <cstring>

MacAddress::MacAddress(const uint8_t* macAddress) {
    memcpy(address, macAddress, sizeof(address));
}

String MacAddress::toString() const {
    char macAsString[18];
    sprintf(macAsString, "%02X:%02X:%02X:%02X:%02X:%02X", 
            address[0], address[1], address[2], 
            address[3], address[4], address[5]);
    return String(macAsString);
}