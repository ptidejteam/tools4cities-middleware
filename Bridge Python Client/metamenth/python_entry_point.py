from py4j.java_gateway import JavaGateway

from metamenth.measure_instruments.meter import Meter
from metamenth.measure_instruments.meter_measure import MeterMeasure
from metamenth.structure.room import Room
from metamenth.structure.open_space import OpenSpace
from metamenth.datatypes.measure import Measure
from metamenth.datatypes.binary_measure import BinaryMeasure
from metamenth.structure.floor import Floor
from metamenth.structure.building import Building
from metamenth.datatypes.address import Address
from metamenth.datatypes.point import Point
from metamenth.transducers.sensor import Sensor
from metamenth.measure_instruments.sensor_data import SensorData
from metamenth.measure_instruments.weather_station import WeatherStation
from metamenth.measure_instruments.weather_data import WeatherData
from metamenth.datatypes.zone import Zone


class PythonEntryPoint:
    """
    Provides an entry point for java client to create
    MetamEnTh objects.
    For example, a Java client can access createRoom with the necessary
    parameter to create a Room object
    """
    def __init__(self):
        pass

    def createMeasure(self, unit, minimum):
        return Measure(unit, minimum)

    def createMeasurement(self, measure, measure_type):
        if measure_type == 'Binary':
            return BinaryMeasure(measure)

    def createRoom(self, area: BinaryMeasure, name: str, room_type: str, location: str):
        return Room(area, name, room_type, JavaGateway(), location)

    def createOpenSpace(self, area: BinaryMeasure, name: str, space_type: str, location: str):
        return OpenSpace(name, area, space_type, JavaGateway(), location)

    def createFloor(self, area: BinaryMeasure, floor_number: float, floor_type: str, height: BinaryMeasure,
                    des: str, room: Room):
        return Floor(area, floor_number, floor_type, JavaGateway(), height, des, room=room)

    def createMeter(self, measurement_freq: float, unit: str, meter_type: str, measure_mode: str):
        return Meter(measurement_freq, unit, meter_type, measure_mode, JavaGateway())

    def createMeasureMeasure(self, value: float):
        return MeterMeasure(value)

    def createCoordinates(self, lat: float, lon: float):
        return Point(lat, lon)

    def createAddress(self, city: str, street: str, state: str, zip_code: str, country: str, coordinates: Point):
        return Address(city, street, state, zip_code, country, coordinates)

    def createBuilding(self, construction_year, height: BinaryMeasure, floor_area: BinaryMeasure, address: Address,
                       building_type: str, floor: Floor):
        return Building(construction_year, height, floor_area, address, building_type, floor, JavaGateway())

    class Java:
        implements = ['com.middleware.metamenth.interfaces.PythonEntryPoint']



