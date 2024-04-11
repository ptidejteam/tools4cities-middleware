from stack import Stack
import time

if __name__ == "__main__":
    stack = Stack()
    start_time = time.time()
    for i in range(150000):
        stack.push(i)
    end_time = time.time()

    print("Total Time: ", end_time - start_time)
