import os
import sys

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

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
        return Room(area, name, room_type, location)

    def createOpenSpace(self, area: BinaryMeasure, name: str, space_type: str, location: str):
        return OpenSpace(name, area, space_type, location)

    def createFloor(self, area: BinaryMeasure, floor_number: float, floor_type: str, height: BinaryMeasure,
                    des: str, room: Room, open_space: OpenSpace):
        return Floor(area, floor_number, floor_type, height, des, room=room, open_space=open_space)

    def createMeter(self, measurement_freq: float, unit: str, meter_type: str, measure_mode: str):
        return Meter(measurement_freq, unit, meter_type, measure_mode)

    def createMeterMeasure(self, value: float, timestamp: str = None):
        return MeterMeasure(value, timestamp)

    def createCoordinates(self, lat: float, lon: float):
        return Point(lat, lon)

    def createAddress(self, city: str, street: str, state: str, zip_code: str, country: str, coordinates: Point):
        return Address(city, street, state, zip_code, country, coordinates)

    def createBuilding(self, construction_year, height: BinaryMeasure, floor_area: BinaryMeasure, address: Address,
                       building_type: str, floor: Floor):
        return Building(construction_year, height, floor_area, address, building_type, floor)

    def createSensor(self, name: str, measure: str, unit: str, measure_type: str, data_frequency: int):
        return Sensor(name, measure, unit, measure_type, data_frequency)

    def createSensorData(self, value: float, timestamp: str):
        return SensorData(value, timestamp)

    def createWeatherStation(self, name: str):
        return WeatherStation(name)

    def createWeatherData(self, data: BinaryMeasure, timestamp: str):
        return WeatherData(data, timestamp)

    def createZone(self, name: str, zone_type: str):
        return Zone(name, zone_type)

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.PythonEntryPoint']
