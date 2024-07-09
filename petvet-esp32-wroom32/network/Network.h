#ifndef NETWORK_H
#define NETWORK_H

#include <Arduino.h>
#include <WiFi.h>

class Network {
public:
    Network();

    void connect(const char* ssid, const char* password);
    bool isConnected();
    void disconnect();

    void extend(const char* ssid, const char* password);
    bool isExtending();
};

#endif // NETWORK_H
