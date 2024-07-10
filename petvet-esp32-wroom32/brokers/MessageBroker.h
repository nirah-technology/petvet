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
    using Callback = std::function<void(const char*)>;

    MessageBroker() = default;
    virtual ~MessageBroker() = default;

    virtual void connect(const char* group, const unsigned int port) = 0;
    virtual bool isConnected() = 0;
    virtual void disconnect() = 0;
    virtual void send(const char* message) = 0;
    virtual void publish(const char* eventType, const char* message) = 0;
    virtual void subscribe(const char* eventType, Callback callback) = 0;
    virtual char* receive() = 0;


protected:
    std::map<std::string, std::vector<Callback>> eventHandlers;
};


#endif // MESSAGEBROKER_H
