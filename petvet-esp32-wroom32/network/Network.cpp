#include "Network.h"

Network::Network() {

}

void Network::connect(const char* ssid, const char* password) {
    WiFi.begin(ssid, password);
}

bool Network::isConnected() {
    return WiFi.status() == WL_CONNECTED;
}

void Network::disconnect() {
    WiFi.
}

void Network::extend(const char* ssid, const char* password) {
    if (this->isConnected()) {
        WiFi.softAP(ssid, password);
    }
}

bool Network::isExtending() {

}
