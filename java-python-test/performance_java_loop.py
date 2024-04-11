from py4j.java_gateway import JavaGateway
import time

if __name__ == "__main__":
    gateway = JavaGateway()

    # Get a reference to the Java Stack
    stack = gateway.entry_point.getStack()

    start_time = time.time()
    stack.pushMultiple(150000)
    end_time = time.time()

    print(stack.getInternalList())
    print("Total Time Java Loop: ", end_time - start_time)
