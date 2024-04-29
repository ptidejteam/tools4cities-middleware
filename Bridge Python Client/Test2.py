import unittest
import subprocess
from py4j.protocol import Py4JJavaError
from hierarchy.dog import Dog
from hierarchy.whale import Whale
from py4j.java_gateway import CallbackServerParameters, GatewayParameters

# Not ideal, the unit test should NOT be dependent on py4j
from py4j.java_gateway import JavaGateway, JavaObject

class TestStringMethods(unittest.TestCase):
    def setUp(self):
        PATH_TO_M2 = "C:/Users/si_ejaz/"
        cmd = "java.exe -cp ../\"Bridge Java Server\"/target/classes;" + PATH_TO_M2 + ".m2/repository/net/sf/py4j/py4j/0.10.9.7/py4j-0.10.9.7.jar com.middleware.StackEntryPoint"
        subprocess.Popen(cmd)
        self.gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                          gateway_parameters=GatewayParameters(auto_convert=True))
        self.stack = self.gateway.entry_point.getStack()

    def tearDown(self):
        self.gateway.shutdown(False)
        pass
        
    def test1(self):
        self.assertNotEqual(self.stack, None)
 
    def test2(self):
        self.assertEqual(type(self.stack), JavaObject)
    
    def test3(self):
        
        try:
            self.stack.pop()
        except Py4JJavaError:
            self.assertTrue(True)
    
    def test4(self):
        dog1 = Dog("Fido", 2)
        self.stack.push(dog1)
        dog2 = self.stack.pop()
        self.assertIsInstance(dog2, JavaObject)
        self.assertEqual(dog1.toString(), dog2.toString())
        
    def test5(self):
        whale1 = Whale("Bob", 2, self.gateway)
        whale2 = Whale("Figo", 3, self.gateway)
        whale1.addChild(whale2)
        
        self.stack.pushAll([whale1, whale2])
        bob = self.stack.pop()
        figo = self.stack.pop()
        
        self.assertEqual(bob.toString(), whale1.toString())
        self.assertEqual(figo.toString(), whale2.toString())
        self.assertEqual(bob.getFirstChild().toString(), whale2.toString())

        
    def test6(self):
        whale1 = Whale("Bob", 2, self.gateway)
        whale2 = Whale("Figo", 3, self.gateway)
        whale1.addChild(whale2)
        
        self.stack.push(whale1)
        bob = self.stack.pop()
        
        self.assertEqual(bob.toString(), whale1.toString())
        self.assertEqual(bob.getFirstChild().toString(), whale2.toString())

