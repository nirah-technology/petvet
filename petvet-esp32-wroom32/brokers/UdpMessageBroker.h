#include <WiFi.h>
#include <WiFiUdp.h>

#include "MessageBroker.h"

class UdpMessageBroker : public MessageBroker {
public:
    UdpMessageBroker();

    // Implémentation de la méthode scan()
    void connect(const char* group, const unsigned int port) override;
    bool isConnected() override;
    void disconnect() override;
    void send(const char* message) override;
    void publish(const char* eventType, const char* message) override;
    void subscribe(const char* eventType, Callback callback) override;
    char* receive() override;

private:
    WiFiUDP udp;
    IPAddress group;
    const unsigned port;
    const unsigned bufferSize = 256;
    char incomingPacket[this->bufferSize];
    bool connected = false;
};

#endif // WIFI_SCANNER_H
