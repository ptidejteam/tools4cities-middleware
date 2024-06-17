from py4j.java_gateway import JavaGateway, CallbackServerParameters
from python_entry_point import PythonEntryPoint


class Server:
    def __init__(self):
        self.gateway = JavaGateway(
            callback_server_parameters=CallbackServerParameters(),
            python_server_entry_point=PythonEntryPoint())

    def start(self):
        print("Python client is ready")
        self.gateway.start_callback_server()  # Start the callback server to handle calls from Java
        input("Press Enter to exit...\n")  # Keep the Python program running
        self.gateway.shutdown()


if __name__ == "__main__":
    server = Server()
    server.start()
