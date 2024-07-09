#ifndef MESSAGEBROKER_H
#define MESSAGEBROKER_H

#include <functional>
#include <map>
#include <vector>
#include <string>
#include <WiFi.h>
#include <WiFiUdp.h>

class MessageBroker {
public:
    MessageBroker();

    void connect(const char* group, const unsigned int port);
    bool isConnected();
    void disconnect();
    void send(const char* message);
    void publish(const char* eventType, const char* message);
    void subscribe(const char* eventType, Callback callback);
    const char* receive();


private:
    WiFiUDP udp;
    std::map<std::string, std::vector<Callback>> subscribers;
};


#endif // MESSAGEBROKER_H
