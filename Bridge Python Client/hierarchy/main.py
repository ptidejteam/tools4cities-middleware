from hierarchy.dog import Dog
from hierarchy.whale import Whale
from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters


if __name__ == "__main__":
    gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                          gateway_parameters=GatewayParameters(auto_convert=True))

    # Create an instance of Dog
    buddy = Dog("Buddy", 1)
    cuddy = Dog("Cuddy", 2)
    duddy = Dog("Duddy", 3)
    blue_whale = Whale('Blue', 15, gateway)

    first_child = Whale('Red', 2, gateway)
    second_child = Whale('Orange', 8, gateway)

    blue_whale.addChild(first_child)
    blue_whale.addChild(second_child)
    first_child.addChild(second_child)
    second_child.addChild(blue_whale)
    # Get a reference to the Java Stack

    stack = gateway.entry_point.getStack()

    # Push the instance onto the Java Stack
    stack.push(buddy)
    stack.push(cuddy)
    stack.push(duddy)
    stack.push("Hello World")
    stack.push(125)
    stack.push(blue_whale)
    stack.push(first_child)
    stack.push(second_child)
    print(stack.pop())
    stack.push({'name': 'Bob', 'age': 5})
    stack.push([1, 2, 3, 4])

    gateway.shutdown()
