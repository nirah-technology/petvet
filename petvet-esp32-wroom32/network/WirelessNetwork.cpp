#include "WirelessNetwork.h"

WirelessNetwork::WirelessNetwork() {

}

void WirelessNetwork::connect(const char* ssid, const char* password) {
    WiFi.mode(WIFI_AP_STA);
    WiFi.begin(ssid, password);
}

bool WirelessNetwork::isConnected() {
    return WiFi.status() == WL_CONNECTED;
}

char* WirelessNetwork::getIPv4() {
    return WiFi.localIP();
}

void WirelessNetwork::disconnect() {
    WiFi.disconnect();
}

void WirelessNetwork::extend(const char* ssid, const char* password) {
    if (this->isConnected()) {
        WiFi.softAP(ssid, password);
        this->isExtended = true;
    }
}

bool WirelessNetwork::isExtending() {
    return this->isExtended;
}
