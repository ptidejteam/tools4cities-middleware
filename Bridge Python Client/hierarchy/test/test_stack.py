from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters
from py4j.java_gateway import JavaObject
from hierarchy.whale import Whale
import unittest
from py4j.java_collections import JavaMap, JavaList
from hierarchy.test import ordered, compare

unittest.defaultTestLoader.sortTestMethodsUsing = compare


class TestStack(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        cls.gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                                  gateway_parameters=GatewayParameters(auto_convert=True))

    @classmethod
    def tearDownClass(cls):
        pass
        cls.gateway.shutdown()

    @ordered
    def test_connection(self):
        self.assertIsNotNone(self.gateway.entry_point)
        self.assertIsInstance(self.gateway.entry_point, JavaObject)

    @ordered
    def test_get_java_stack(self):
        stack = self.gateway.entry_point.getStack()
        initial_item = stack.pop()
        self.assertIsInstance(stack, JavaObject)
        self.assertEqual(initial_item, 'Initial Item')

    @ordered
    def test_push_dictionary_to_stack(self):
        stack = self.gateway.entry_point.getStack()
        dictionary = {'greeting': 'Hello from Python'}
        stack.push(dictionary)
        java_dict = stack.getInternalList()[0]
        self.assertEqual(java_dict['greeting'], 'Hello from Python')

    @ordered
    def test_push_list_to_stack(self):
        stack = self.gateway.entry_point.getStack()
        animal_names = ['dog', 'cat', 'whale']
        stack.push(animal_names)
        stack_item = stack.pop()
        self.assertIsInstance(stack_item, JavaMap)
        self.assertEqual(stack_item.get("greeting"), 'Hello from Python')
        stack_item = stack.pop()
        self.assertIsInstance(stack_item, JavaList)
        self.assertEqual(stack_item.get(0), 'dog')
        self.assertEqual(stack_item.get(1), 'cat')

    @ordered
    def test_add_whale(self):
        blue_whale = Whale('Blue', 15, self.gateway)
        first_child = Whale('Red', 2, self.gateway)

        blue_whale.addChild(first_child)

        stack = self.gateway.entry_point.getStack()
        stack.pushAll([blue_whale, first_child])
        java_blue_whale = stack.pop()
        java_blue_whale_first_child = java_blue_whale.getFirstChild()
        self.assertEqual(java_blue_whale.toString(), blue_whale.toString())
        self.assertEqual(java_blue_whale_first_child.toString(), first_child.toString())


if __name__ == '__main__':
    loader = unittest.TestLoader()
    tests = loader.loadTestsFromTestCase(TestStack)

    test_suite = unittest.TestSuite(tests)
    unittest.TextTestRunner().run(test_suite)
