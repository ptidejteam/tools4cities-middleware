from py4j.java_gateway import JavaGateway
import time

if __name__ == "__main__":
    gateway = JavaGateway()

    # Get a reference to the Java Stack
    stack = gateway.entry_point.getStack()
    start_time = time.time()
    for i in range(150000):
        stack.push(i)
    end_time = time.time()
    print("Total Time Python Loop: ", end_time - start_time)



