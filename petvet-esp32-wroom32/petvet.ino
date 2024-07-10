#include <Arduino.h>
#include <WiFi.h>
#include "ModeType.h"
#include "Scanner.h"
#include "WiFiScanner.h"
#include "BluetoothScanner.h"
#include "WirelessNetwork.h"
#include "MessageBroker.h"
#include "UdpMessageBroker.h"

// Specific configuration

const String ESP_ID = "{{ esp.id }}";
const String WIFI_SSID = "{{ wifi.ssid }}";
const String WIFI_WPA = "{{ wifi.wpa }}";
const String MULTICAST_GROUP = "{{ multicast.group }}";
const unsigned int MULTICAST_PORT = {{ multicast.port }};

ModeType mode = ModeType::NATIVE_NODE;

const Scanner scanner;
const String macAddress;
const MessageBroker messageBorker;

unsigned long uptime;
unsigned long lastScanExecutionOrder;
unsigned long lastReceivedOrchestratorAvailabilityResponse;
unsigned long lastReceivedScanExecutionOrder;
unsigned long lastSendedHeartBeat;

const WirelessNetwork network;

void connectToWifFi(const char* ssid, const char* password) {
    network = WirelessNetwork();
    network.connect(ssid, password);
    while (!network.isConnected()) {
        delay(1000);
        Serial.println("Connecting to WiFi...");
    }
    Serial.println("Connected to WiFi.");
    Serial.println("Extending WiFi...");
    network.extend(ssid, password);
    Serial.println("WiFi extended.");
}

void connectToMessageBroker(const char* group, const unsigned int port) {
    messageBorker = UdpMessageBroker();
    messageBorker.connect(group, port);
    Serial.println("Connected to UDP Message Broker.");
}

void askIfOrchestratorIsAvailable() {
    
}

void setup() {
    Serial.begin(115200);
    connectToWifFi(SSID, WPA);
    connectToMessageBroker(GROUP, PORT);
}

void requestChallenge() {
    // System.out.println(String.format("[%s] I want to elect a new orchestrator...", this.id));
    // ChallengeOrchestratorMessage message = ChallengeOrchestratorMessage.create(id, mac, ip, this.mode.get());
    // try {
    //     this.messageBroker.send(message);
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }
    // this.lastReceivedOrchestratorAvailabilityResponse = LocalDateTime.now();
    // this.lastReceivedScanExecutionOrder = this.lastReceivedOrchestratorAvailabilityResponse;
}

bool isScanRequestInLate() {
    return false;
    // if (Objects.isNull(this.lastReceivedScanExecutionOrder)) {
    //     return false;
    // }
    // return LocalDateTime.now().isAfter(this.lastReceivedScanExecutionOrder.plus(this.intervalBetweenEachScans.plusSeconds(1)));
}

boolean isHeartBeatInLate() {
    return false;
    // if (Objects.isNull(this.lastSendedHeartBeat)) {
    //     return false;
    // }
    // return LocalDateTime.now().isAfter(this.lastSendedHeartBeat.plus(this.intervalBetweenEachHeartBeat));
}

void sendHeartBeat() {
    // final Command heartBeat = CommandFactory.createHeartBeatCommand(messageBroker, id, mac, ip, this.mode.get(), this.uptime);
    // try {
    //     heartBeat.execute();
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }
    // this.lastSendedHeartBeat = LocalDateTime.now();
}

void triggerNativeTask() {
    // try {
    //     this.messageBroker.receive();
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }

    if (this.isScanRequestInLate()) {
    //     System.out.println(String.format("[%s] It's seem that scan request is in late...", this.id));
        this.requestChallenge();
    }

    if (this.isHeartBeatInLate()) {
    //     System.out.println(String.format("[%s] I'm alive!", this.id));
        this.sendHeartBeat();
    }
}

void sendScanNowRequest() {
    // final ScanNowMessage message = ScanNowMessage.create(
    //     this.id, this.mac, this.ip, this.mode.get() 
    // );
    // try {
    //     this.messageBroker.send(message);
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }
    // this.lastScanExecutionOrder = LocalDateTime.now();

}

void sendScanNowMessageIfRequired() {
    // if (Objects.isNull(this.lastScanExecutionOrder)) {
    //     System.out.println(String.format("[%s] Scan is required!", this.id));
    //     this.sendScanNowRequest();
    // } else {
    //     if (LocalDateTime.now().isAfter(this.lastScanExecutionOrder
    //             .plus(this.intervalBetweenEachScans.toMillis(), ChronoUnit.MILLIS))) {
    //         System.out.println(String.format("[%s] Another scan must be executed!", this.id));
    //         this.sendScanNowRequest();
    //     }
    // }
}


void triggerOrchestratorTask() {
    sendScanNowMessageIfRequired();
    triggerNativeTask();
}

void loop() {
    switch (mode) {
        case ModeType::NATIVE_NODE:
            triggerNativeTask();
            break;
        case ModeType::ORCHESTRATOR_NODE:
            triggerOrchestratorTask();
            break;
        default:
            Serial.println("Unknown Mode...");
            break;
    }

}