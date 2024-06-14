from socket import socket as Socket, AF_INET, SOCK_DGRAM, IPPROTO_UDP, IPPROTO_IP, IP_MULTICAST_TTL

class Messages:
    IS_ORCHESTRATOR_AVAILABLE="IS_ORCHESTRATOR_AVAILABLE"
    ORCHESTRATOR_AVAILABLE="ORCHESTRATOR_AVAILABLE"
    SCAN_NOW="SCAN_NOW"
    CHALLENGE_ORCHESTRATOR="CHALLENGE_ORCHESTRATOR"
    VOTE="VOTE"
    SCAN_REPORT="SCAN_REPORT"

class Sketch:
    def __init__(self) -> None:
        self.__is_running: bool = False
        self.__multicast_group_address: str = "224.6.6.6"
        self.__multicast_group_port: int = 44666
        self.__multicast_socket: Socket

    def __initiate_multicast_socket(self):
        if (self.__multicast_socket is None) :
            self.__multicast_socket = Socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP)
            self.__multicast_socket.setsockopt(IPPROTO_IP, IP_MULTICAST_TTL)

    def __send_message(self, message: str):
        self.__initiate_multicast_socket()
        self.__multicast_socket.send(message, (self.__multicast_group_address, self.__multicast_group_port))


    def run(self):
        self.__initiate_multicast_socket()
        self.__send_message(Messages.IS_ORCHESTRATOR_AVAILABLE)
        self.__multicast_socket.settimeout()

        self.__is_running = True
        while (self.__is_running):
            pass


class ESP32:
    def __init__(self, program: Sketch) -> None:
        self.__program: Sketch = program

    def run_program(self):
        if (self.__program is not None):
            self.__program.run()

if (__name__ == "__main__"):
    program: Sketch = Sketch()
    esp: ESP32 = ESP32(program)
    esp.run_program()
