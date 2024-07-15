import os
import subprocess
import time
import unittest
from dotenv import load_dotenv
from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters
from metamenth.tests.util import JavaEnums
from metamenth.python_entry_point import PythonEntryPoint


# Not ideal, the unit test should NOT be dependent on py4j
class TestPythonEntryPoint(unittest.TestCase):

    def start_python_server(self):
        # Load environment variables from .env file
        self.gateway = JavaGateway(
            callback_server_parameters=CallbackServerParameters(),
            python_server_entry_point=PythonEntryPoint())
        self.gateway.start_callback_server()

    def start_java_client(self):
        load_dotenv()

        # Command to start the Java server
        path_to_m2 = os.getenv('PATH_TO_M2')

        if not os.path.exists(path_to_m2):
            raise ValueError(
                "The path to .m2 doesn't exist, please create/edit the .env file in the root folder of this project")
            pass

        command = ["java", "-cp",
                   os.path.abspath("../../../Bridge Java Server/target/classes") + os.pathsep + path_to_m2 +
                   ".m2/repository/net/sf/py4j/py4j/0.10.9.7/py4j-0.10.9.7.jar",
                   "ca.concordia.ngci.tools4cities.metamenth.PythonEntryServer"]

        self._java_client = subprocess.Popen(command)
        # Wait for Java to start
        time.sleep(0.25)

    def setUp(self):
        self.start_python_server()
        self.start_java_client()
        self.enums = JavaEnums(self.gateway)

    def tearDown(self):
        self.gateway.shutdown()
        self._java_client.terminate()
        self._java_client.wait()

    def test_get_building(self):
        gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True))
        building = gateway.entry_point.getLBBuilding()
        self.assertIsNotNone(building)
        self.assertEqual(len(building.getWeatherStation('LB WS').getWeatherData({})), 10)
        meter = building.getMeters({'MeterType': 'Electricity'})[0]
        self.assertEqual(meter.getMeterType(), self.enums.MeterType.ELECTRICITY.getValue())
        self.assertEqual(len(meter.getMeterMeasures({})), 0)
        self.assertIsNotNone(building.getAddress())
        self.assertEqual(building.getAddress().getCity(), 'Montreal')
        self.assertEqual(building.getAddress().getZipCode(), 'H3G 1M8')
        self.assertEqual(building.getFloorArea().getValue(), 50591.3)
        floor = building.getFloorByNumber(1)
        self.assertEqual(len(floor.getRooms({})), 1)
        self.assertIsNone(floor.getRoomByName('ROOM 54'))
        room = floor.getRoomByName('Room 001')
        self.assertEqual(room.getRoomType(), self.enums.RoomType.OFFICE.getValue())
        self.assertIsNotNone(room.getTransducer('TMP 01'))
        self.assertEqual(room.getTransducer('TMP 01').getUnit(), self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue())
        self.assertEqual(room.getTransducer('TMP 01').getDataFrequency(), 900)
        self.assertEqual(len(room.getTransducer('TMP 01').getData({})), 10)
        self.assertEqual(room.getTransducer('TMP 01').getData({})[0].getValue(), 10.0)
        self.assertIsNone(building.getFloorByNumber(2))

