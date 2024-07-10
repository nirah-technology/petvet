#include "UdpMessageBroker.h"

UdpMessageBroker::UdpMessageBroker()
{
}

void UdpMessageBroker::connect(const char *group, const unsigned int port)
{
    this->group = IPAddress(group);
    this->port = port
    this->udp.beginMulticast(WiFi.localIP(), this->group, this->port);
    this->isConnected = true;
}

bool UdpMessageBroker::isConnected()
{
    return this->isConnected;
}

void UdpMessageBroker::disconnect()
{
    this->udp.stop();
    this->isConnected = false;
}

void UdpMessageBroker::send(const char* message)
{
    udp.beginPacketMulticast(this->group, this->port, WiFi.localIP());
    udp.write(message);
    udp.endPacket();
}

void UdpMessageBroker::publish(const char *eventType, const char *message)
{
    std::string event(eventType);
    auto it = this->eventHandlers.find(event);
    if (it != this->eventHandlers.end()) {
        for (auto& callback : it->second) {
            callback(message);
        }
    }
}

void UdpMessageBroker::subscribe(const char *eventType, Callback callback)
{
    std::string event(eventType);
    this->eventHandlers[event].push_back(callback);
}

char *UdpMessageBroker::receive()
{
    int packetSize = udp.parsePacket();
    if (packetSize)
    {
        memset(this->incomingPacket, 0, sizeof(this->incomingPacket)); // Réinitialiser le buffer
        int len = this->udp.read(this->incomingPacket, sizeof(this->incomingPacket) - 1);
        if (len > 0)
        {
            this->incomingPacket[len] = 0; // Terminer la chaîne avec un caractère nul
        }
        return this->incomingPacket;
    }
    return nullptr;
}
