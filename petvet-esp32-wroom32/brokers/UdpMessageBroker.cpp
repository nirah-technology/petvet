#include "UdpMessageBroker.h"

UdpMessageBroker::MessageBroker()
{
}

void UdpMessageBroker::connect(const char *group, const unsigned int port)
{
    udp.beginMulticast(WiFi.localIP(), IPAddress(group), multicastPort);
}

bool UdpMessageBroker::isConnected()
{
}

void UdpMessageBroker::disconnect()
{
}

void UdpMessageBroker::send(const char *message)
{
}

void UdpMessageBroker::publish(const char *eventType, const char *message)
{
}

void UdpMessageBroker::subscribe(const char *eventType, Callback callback)
{
}

char *UdpMessageBroker::receive()
{
    int packetSize = udp.parsePacket();
    if (packetSize)
    {
        memset(this->incomingPacket, 0, sizeof(this->incomingPacket)); // Réinitialiser le buffer
        int len = udp.read(this->incomingPacket, sizeof(this->incomingPacket) - 1);
        if (len > 0)
        {
            incomingPacket[len] = 0; // Terminer la chaîne avec un caractère nul
        }
        return this->incomingPacket;
    }
    return nullptr;
}
