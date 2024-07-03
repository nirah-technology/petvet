const int HELLO_INTERVAL_IN_MILLISECONDS = 1000;
unsigned long lastCheckTimestampInMillis = 0;

void setup() {
  Serial.begin(115200);
}

bool isAllowedToSendHello() {
  bool isAllowed = false;
  unsigned long now = millis();
  if (now >= lastCheckTimestampInMillis+HELLO_INTERVAL_IN_MILLISECONDS) {
    isAllowed = true;
    lastCheckTimestampInMillis = now;
  }
  return isAllowed;
}

void sendHelloToSerial() {
  Serial.println("<id=1908eb78-ddcd-4b1f-a74a-66f80769f215;softwareName=TOTO;softwareVersion=1.2.3-RELEASE>");
}

void loop() {
    if (isAllowedToSendHello()) {
        sendHelloToSerial();
    }
}