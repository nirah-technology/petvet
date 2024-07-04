const String ID = "{{ esp.id }}";
const String SOTFWARE_NAME = "{{esp.software.name}}";
const String SOTFWARE_VERSION = "{{esp.software.version}}";
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
  Serial.println("<id="+ID+";softwareName="+SOTFWARE_NAME+";softwareVersion="+SOTFWARE_VERSION+">");
}

void loop() {
    if (isAllowedToSendHello()) {
        sendHelloToSerial();
    }
}