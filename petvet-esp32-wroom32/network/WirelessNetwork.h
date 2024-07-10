#ifndef WIRELESSNETWORK_H
#define WIRELESSNETWORK_H

#include <Arduino.h>
#include <WiFi.h>

class WirelessNetwork {
public:
    WirelessNetwork();

    void connect(const char* ssid, const char* password);
    bool isConnected();
    char* getIPv4();
    void disconnect();

    void extend(const char* ssid, const char* password);
    bool isExtending();

private:
    bool isExtended = false;
};

#endif // WIRELESSNETWORK_H
