import os
import socket
import subprocess
import time
import unittest
from dotenv import load_dotenv
from py4j.java_gateway import JavaGateway, JavaObject, CallbackServerParameters, GatewayParameters
from py4j.protocol import Py4JError
from metamenth.measure_instruments.meter import Meter
from metamenth.structure.room import Room
from metamenth.structure.open_space import OpenSpace
from metamenth.datatypes.measure import Measure
import copy
from metamenth.datatypes.binary_measure import BinaryMeasure
from metamenth.structure.floor import Floor
from metamenth.structure.building import Building
from metamenth.datatypes.address import Address
from metamenth.datatypes.point import Point
from metamenth.transducers.sensor import Sensor
from metamenth.measure_instruments.sensor_data import SensorData
from metamenth.datatypes.interfaces.abstract_range_measure import AbstractRangeMeasure
from metamenth.measure_instruments.weather_data import WeatherData
from metamenth.measure_instruments.weather_station import WeatherStation
from metamenth.tests.util import JavaEnums


# Not ideal, the unit test should NOT be dependent on py4j
class TestMetamenthEntryPoint(unittest.TestCase):
    def is_port_available(self, host, port):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(2)  #Timeout in case of port not open
        try:
            s.connect((host, port))  #Port ,Here 22 is port
            return True
        except:
            return False

    def start_server(self):
        # Load environment variables from .env file
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
                   "com.middleware.MetamenthEntryPoint"]

        # Start the Java server as a subprocess
        subprocess.Popen(command)
        # Wait for the Java server to have started
        while self.is_port_available("localhost", 25334):
            pass

    def setUp(self):
        self.start_server()
        self.gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                                   gateway_parameters=GatewayParameters(auto_convert=True))

        self.repo = self.gateway.entry_point.getMetamenthRepository()
        self.enums = JavaEnums(self.gateway)

    def tearDown(self):
        self.gateway.shutdown()

    def test_get_metamenth_repo(self):
        self.assertNotEqual(self.repo, None)

    def test_get_non_existing_repo_method(self):
        try:
            self.repo.getRoom()
        except Py4JError as err:
            pass

    def test_get_non_existing_entity(self):
        null_obj = self.repo.getEntity("address")
        self.assertIsNone(null_obj)

    def test_get_non_existing_java_enum(self):
        try:
            _ = self.enums.OFFICE_TYPE.OPENED.getValue()
        except TypeError as err:
            self.assertEqual(err.__str__(), "'JavaPackage' object is not callable")

    def test_send_nothing(self):
        try:
            self.repo.addEntity()
        except Py4JError as err:
            self.assertNotEqual(err.__str__().find('Method addEntity([]) does not exist'), -1)

    def test_receive_nothing(self):
        try:
            self.repo.getEntity()
        except Py4JError as err:
            self.assertNotEqual(err.__str__().find('Method getEntity([]) does not exist'), -1)

    def test_exchange_binary_measure(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        self.repo.addEntity(height)
        binary_measure = self.repo.getEntity('binary_measure')
        self.assertIsInstance(binary_measure, JavaObject)
        self.assertEqual(binary_measure.toString(), height.toString())

    def test_exchange_address(self):
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
        self.repo.addEntity(address)
        address.getGeocoordinates().setLatitude(4.3898238)
        received_address_obj = self.repo.getEntity("address")
        self.assertEqual(received_address_obj.toString(), address.toString())

    def test_exchange_address_with_updates(self):
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
        self.repo.addEntity(address)
        address_copy = copy.deepcopy(address)
        address_copy.getGeocoordinates().setLatitude(4.3898238)
        received_address_obj = self.repo.getEntity("address")
        self.assertEqual(received_address_obj.getGeocoordinates().toString(), address.getGeocoordinates().toString())
        self.assertNotEqual(received_address_obj.toString(), address_copy.toString())

    def test_exchange_sensor_without_data(self):
        sensor = Sensor(name='TMP.01', measure=self.enums.SensorMeasure.TEMPERATURE.getValue(), data_frequency=90,
                        unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(),
                        measure_type=self.enums.SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(),
                        sensor_log_type=self.enums.SensorLogType.POLLING.getValue(), gateway=self.gateway)

        self.repo.addEntity(sensor)
        received_sensor = self.repo.getEntity("sensor")
        self.assertEqual(sensor.getSensorLogType(), received_sensor.getSensorLogType())
        self.assertEqual(sensor.getName(), received_sensor.getName())
        self.assertEqual(sensor.getMetaData(), received_sensor.getMetaData())
        self.assertEqual(sensor.toString(), received_sensor.toString())

    def test_exchange_sensor_with_data(self):
        sensor = Sensor(name='TMP.01', measure=self.enums.SensorMeasure.TEMPERATURE.getValue(), data_frequency=90,
                        unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(),
                        measure_type=self.enums.SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(),
                        sensor_log_type=self.enums.SensorLogType.POLLING.getValue(), gateway=self.gateway)
        sensor.addData([SensorData(15), SensorData(16.5), SensorData(27.5)])
        sensor.setCurrentValue(26)

        measure = Measure(unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(), minimum=5, maximum=40)
        sensor_measure_range = AbstractRangeMeasure(measure)
        sensor.setMeasureRange(sensor_measure_range)

        self.repo.addEntity(sensor)
        received_sensor = self.repo.getEntity("sensor")
        self.assertEqual(sensor.getMeasureType(), received_sensor.getMeasureType())
        self.assertEqual(sensor.getMetaData(), received_sensor.getMetaData())
        self.assertEqual(sensor.toString(), received_sensor.toString())
        self.assertEqual(sensor.getMeasureRange().getMeasurementUnit(),
                         received_sensor.getMeasureRange().getMeasurementUnit())

    def test_exchange_invalid_address(self):
        try:
            address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                              country=None, geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
            self.repo.addEntity(address)
        except ValueError as err:
            self.assertEqual(err.__str__(), 'country is required')

    def test_exchange_room(self):
        measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        area = BinaryMeasure(measure)
        room = Room(area, name="STD 101", room_type=self.enums.RoomType.STUDY_ROOM.getValue(), location='hre.vrs.ies')
        room_copy = copy.deepcopy(room)
        room_copy.setRoomType(self.enums.RoomType.BEDROOM.getValue())
        self.repo.addEntity(room)
        self.repo.addEntity(room_copy)
        received_room_obj = self.repo.getEntity("room")
        self.assertNotEqual(received_room_obj.getRoomType(), room.getRoomType())
        self.assertEqual(received_room_obj.getRoomType(), room_copy.getRoomType())
        self.assertNotEqual(received_room_obj.toString(), room.toString())
        self.assertEqual(received_room_obj.toString(), room_copy.toString())

    def test_exchange_room_with_meter(self):
        meter = Meter('hre.vrs.ies', 0.5, self.enums.MeasurementUnit.KILOWATTS.getValue(),
                      self.enums.MeterType.ELECTRICITY.getValue(), self.enums.MeterMeasureMode.AUTOMATIC.getValue(),
                      False)
        measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        area = BinaryMeasure(measure)
        room = Room(area, name="STD 101", room_type=self.enums.RoomType.STUDY_ROOM.getValue(), location='hre.vrs.ies')
        room.setMeter(meter)
        self.repo.addEntity(room)
        received_room_obj = self.repo.getEntity("room")
        self.assertEqual(received_room_obj.getMeter().toString(), meter.toString())
        self.assertEqual(received_room_obj.toString(), room.toString())
        self.assertNotEqual(received_room_obj, room)
        self.assertIsInstance(room, Room)
        self.assertIsInstance(received_room_obj, JavaObject)

    def test_exchange_room_with_meter_and_sensor(self):
        meter = Meter('hre.vrs.ies', 0.5, self.enums.MeasurementUnit.KILOWATTS.getValue(),
                      self.enums.MeterType.ELECTRICITY.getValue(), self.enums.MeterMeasureMode.AUTOMATIC.getValue(),
                      False)
        measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        area = BinaryMeasure(measure)
        room = Room(area, name="STD 101", room_type=self.enums.RoomType.STUDY_ROOM.getValue(), location='hre.vrs.ies')
        room.setMeter(meter)

        sensor = Sensor(name='TMP.01', measure=self.enums.SensorMeasure.TEMPERATURE.getValue(), data_frequency=90,
                        unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(),
                        measure_type=self.enums.SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(),
                        sensor_log_type=self.enums.SensorLogType.POLLING.getValue(), gateway=self.gateway)

        sensor.addData([SensorData(15), SensorData(16.5), SensorData(27.5), SensorData(20)])
        room.addTransducer(sensor)

        self.repo.addEntity(room)
        received_room_obj = self.repo.getEntity("room")
        self.assertEqual(received_room_obj.getMeter().toString(), meter.toString())
        self.assertEqual(received_room_obj.getName(), room.getName())
        self.assertEqual(room.getTransducer(sensor.getName()).toString(),
                         received_room_obj.getTransducer(sensor.getName()).toString())

    def test_exchange_open_space_but_get_room(self):
        measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        area = BinaryMeasure(measure)
        hall = OpenSpace(name="Hall", area=area, space_type=self.enums.SpaceType.HALL.getValue())
        self.repo.addEntity(hall)
        room = self.repo.getEntity("room")
        self.assertIsNone(room)

    def test_exchange_open_space(self):
        measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        area = BinaryMeasure(measure)
        hall = OpenSpace(name="Hall", area=area, space_type=self.enums.SpaceType.HALL.getValue())
        self.repo.addEntity(hall)
        received_space_obj = self.repo.getEntity("open_space")
        self.assertEqual(received_space_obj.toString(), hall.toString())

    def test_exchange_floor_with_no_spaces(self):
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        try:
            floor = Floor(area=floor_area, number=2, floor_type=self.enums.FloorType.REGULAR.getValue())
            self.repo.addEntity(floor)
        except ValueError as err:
            self.assertEqual(err.__str__(), "A floor must have at least one room or one open space.")

    def test_exchange_floor(self):
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        hall = OpenSpace(name="Hall", area=floor_area, space_type=self.enums.SpaceType.HALL.getValue())
        floor = Floor(area=floor_area, number=2, floor_type=self.enums.FloorType.REGULAR.getValue(), open_space=hall)
        self.repo.addEntity(floor)
        received_floor_obj = self.repo.getEntity("floor")
        self.assertEqual(received_floor_obj.getHeight(), None)
        self.assertEqual(received_floor_obj.getNumber(), 2)
        self.assertEqual(received_floor_obj.getDescription(), None)
        self.assertEqual(received_floor_obj.toString(), floor.toString())

    def test_exchange_building_with_no_floor(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
        try:
            _ = Building(construction_year=2024, height=height, floor_area=floor_area,
                         building_type=self.enums.BuildingType.COMMERCIAL.getValue(), address=address, floor=None,
                         gateway=self.gateway)
        except ValueError as err:
            self.assertEqual(err.__str__(), "floors must be of type Floor")

    def test_exchange_building(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
        hall = OpenSpace(name="Hall", area=floor_area, space_type=self.enums.SpaceType.HALL.getValue())
        floor = Floor(area=floor_area, number=1, floor_type=self.enums.FloorType.REGULAR.getValue(), open_space=hall)
        building = Building(construction_year=2024, height=height, floor_area=floor_area,
                            building_type=self.enums.BuildingType.COMMERCIAL.getValue(), address=address, floor=floor,
                            gateway=self.gateway)
        self.repo.addEntity(building)
        received_building_obj = self.repo.getEntity('building')
        self.assertEqual(received_building_obj.getHeight().toString(), building.getHeight().toString())
        self.assertEqual(received_building_obj.getFloorArea().toString(), building.getFloorArea().toString())
        self.assertEqual(received_building_obj.getBuildingType(), building.getBuildingType())
        self.assertEqual(received_building_obj.toString(), building.toString())

    def test_exchange_building_with_weather_station(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.839243293, lon=-1.389883929))
        hall = OpenSpace(name="Hall", area=floor_area, space_type=self.enums.SpaceType.HALL.getValue())
        floor = Floor(area=floor_area, number=0, floor_type=self.enums.FloorType.BASEMENT.getValue(), open_space=hall)
        building = Building(construction_year=1999, height=height, floor_area=floor_area,
                            building_type=self.enums.BuildingType.RESIDENTIAL.getValue(), address=address, floor=floor,
                            gateway=self.gateway)

        weather_station = WeatherStation('WS 01', self.gateway)
        building.addWeatherStation(weather_station)
        self.repo.addEntity(building)
        received_building_obj = self.repo.getEntity('building')
        self.assertEqual(received_building_obj.getWeatherStation(weather_station.getName()).toString(),
                         building.getWeatherStation(weather_station.getName()).toString())
        self.assertEqual(received_building_obj.getAddress().getCity(), building.getAddress().getCity())
        self.assertEqual(received_building_obj.toString(), building.toString())

    def test_exchange_building_with_weather_station_and_data(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.839243293, lon=-1.389883929))
        hall = OpenSpace(name="Hall", area=floor_area, space_type=self.enums.SpaceType.HALL.getValue())
        floor = Floor(area=floor_area, number=0, floor_type=self.enums.FloorType.BASEMENT.getValue(), open_space=hall)
        building = Building(construction_year=1999, height=height, floor_area=floor_area,
                            building_type=self.enums.BuildingType.RESIDENTIAL.getValue(), address=address, floor=floor,
                            gateway=self.gateway)

        weather_station = WeatherStation('WS 01', self.gateway)
        temp_measure_1 = BinaryMeasure(Measure(unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(), minimum=20))
        temp_measure_2 = copy.deepcopy(temp_measure_1)
        temp_measure_2.setValue(30)

        weather_station.addWeatherData([WeatherData(temp_measure_1), WeatherData(temp_measure_2)])

        building.addWeatherStation(weather_station)
        self.repo.addEntity(building)
        received_building_obj = self.repo.getEntity('building')
        self.assertEqual(received_building_obj.getWeatherStation(weather_station.getName()).getWeatherData(),
                         building.getWeatherStation(weather_station.getName()).getWeatherData())
        self.assertEqual(received_building_obj.toString(), building.toString())

    def test_remove_weather_station_from_building(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.839243293, lon=-1.389883929))
        hall = OpenSpace(name="Hall", area=floor_area, space_type=self.enums.SpaceType.HALL.getValue())
        floor = Floor(area=floor_area, number=0, floor_type=self.enums.FloorType.BASEMENT.getValue(), open_space=hall)
        building = Building(construction_year=1999, height=height, floor_area=floor_area,
                            building_type=self.enums.BuildingType.RESIDENTIAL.getValue(), address=address, floor=floor,
                            gateway=self.gateway)

        weather_station = WeatherStation('WS 01', self.gateway)
        temp_measure_1 = BinaryMeasure(Measure(unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(), minimum=20))
        temp_measure_2 = copy.deepcopy(temp_measure_1)
        temp_measure_2.setValue(30)

        weather_station.addWeatherData([WeatherData(temp_measure_1), WeatherData(temp_measure_2)])

        building.addWeatherStation(weather_station)
        self.repo.addEntity(building)
        received_building_obj = self.repo.getEntity('building')
        ws = received_building_obj.getWeatherStation(weather_station.getName())
        self.assertTrue(received_building_obj.removeWeatherStation(ws))
        self.assertIsNone(received_building_obj.getWeatherStation(weather_station.getName()))
        self.assertEqual(received_building_obj.toString(), building.toString())

    def test_get_building(self):
        height_measure = Measure(unit=self.enums.MeasurementUnit.METERS.getValue(), minimum=50)
        height = BinaryMeasure(height_measure)
        area_measure = Measure(unit=self.enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
        floor_area = BinaryMeasure(area_measure)
        address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC',
                          country='Canada',
                          geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))

        sensor = Sensor(name='TMP.01', measure=self.enums.SensorMeasure.TEMPERATURE.getValue(), data_frequency=90,
                        unit=self.enums.MeasurementUnit.DEGREE_CELSIUS.getValue(),
                        measure_type=self.enums.SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(),
                        sensor_log_type=self.enums.SensorLogType.POLLING.getValue(), gateway=self.gateway)

        hall = OpenSpace(name="Hall", area=floor_area, space_type=self.enums.SpaceType.HALL.getValue())
        room = Room(floor_area, name="STD 101", room_type=self.enums.RoomType.STUDY_ROOM.getValue(),
                    location='hre.vrs.ies')
        floor = Floor(area=floor_area, number=2, floor_type=self.enums.FloorType.REGULAR.getValue(), open_space=hall,
                      room=room)
        building = Building(construction_year=2024, height=height, floor_area=floor_area,
                            building_type=self.enums.BuildingType.COMMERCIAL.getValue(), address=address, floor=floor,
                            gateway=self.gateway)

        self.repo.addEntity(building)

        room_copy = copy.deepcopy(room)
        floor_copy = copy.deepcopy(floor)
        self.repo.addEntity(room_copy)
        self.repo.addEntity(floor_copy)
        self.repo.addEntity(sensor)

        received_building_obj = self.repo.getBuilding()
        self.assertEqual(received_building_obj.getConstructionYear(), 2025)
        self.assertEqual(building.getConstructionYear(), received_building_obj.getConstructionYear())
        self.assertEqual(building.getAddress().toString(), received_building_obj.getAddress().toString())
        self.assertEqual(received_building_obj.toString(), building.toString())
