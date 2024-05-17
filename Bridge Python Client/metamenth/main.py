from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters

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
from metamenth.measure_instruments.weather_station import WeatherStation
from metamenth.measure_instruments.weather_data import WeatherData


class Utils:
    def __init__(self, gateway):
        self.gateway = gateway
        self.FloorType = gateway.jvm.com.middleware.enums.FloorType
        self.SpaceType = gateway.jvm.com.middleware.enums.OpenSpaceType
        self.RoomType = gateway.jvm.com.middleware.enums.RoomType
        self.MeterType = gateway.jvm.com.middleware.enums.MeterType
        self.MeasurementUnit = gateway.jvm.com.middleware.enums.MeasurementUnit
        self.MeterMeasureMode = gateway.jvm.com.middleware.enums.MeterMeasureMode
        self.BuildingType = gateway.jvm.com.middleware.enums.BuildingType
        self.SensorMeasure = gateway.jvm.com.middleware.enums.SensorMeasure
        self.SensorMeasureType = gateway.jvm.com.middleware.enums.SensorMeasureType
        self.SensorLogType = gateway.jvm.com.middleware.enums.SensorLogType


if __name__ == "__main__":
    gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                          gateway_parameters=GatewayParameters(auto_convert=True))

    repo = gateway.entry_point.getMetamenthRepository()
    enums = Utils(gateway)

    meter = Meter('hre.vrs.ies', 0.5, enums.MeasurementUnit.KILOWATTS.getValue(),
                  enums.MeterType.ELECTRICITY.getValue(), enums.MeterMeasureMode.AUTOMATIC.getValue(), False)
    measure = Measure(unit=enums.MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
    area = BinaryMeasure(measure)

    sensor = Sensor(name='TMP.01', measure=enums.SensorMeasure.TEMPERATURE.getValue(), data_frequency=90,
                    unit=enums.MeasurementUnit.DEGREE_CELSIUS.getValue(),
                    measure_type=enums.SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(),
                    sensor_log_type=enums.SensorLogType.POLLING.getValue(), gateway=gateway)

    # add some data to the sensor
    sensor.addData([SensorData(14), SensorData(15), SensorData(16.8), SensorData(25.8), SensorData(10.19)])

    room = Room(area, name="STD 101", room_type=enums.RoomType.STUDY_ROOM.getValue(), location='hre.vrs.ies')
    hall = OpenSpace(name="Hall", area=area, space_type=enums.SpaceType.HALL.getValue())
    room.setMeter(meter)
    floor = Floor(area=area, number=2, floor_type=enums.FloorType.REGULAR.getValue(), room=room, open_space=hall)

    height_measure = Measure(unit=enums.MeasurementUnit.METERS.getValue(), minimum=50)
    height = BinaryMeasure(height_measure)
    area.setValue(1100)
    address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC', country='Canada',
                      geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
    building = Building(construction_year=2024, height=height, floor_area=area,
                        building_type=enums.BuildingType.COMMERCIAL.getValue(), address=address, floor=floor,
                        gateway=gateway)

    weather_station = WeatherStation('WS 01', gateway)
    temp_measure = BinaryMeasure(Measure(unit=enums.MeasurementUnit.DEGREE_CELSIUS.getValue(), minimum=20))
    weather_station.addWeatherData([WeatherData(temp_measure)])
    building.addWeatherStation(weather_station)

    # provide unique object types which middleware could use to "construct" a building
    repo.addEntity(building)
    new_room = copy.deepcopy(room)
    repo.addEntity(new_room)
    repo.addEntity(sensor)
    new_floor = copy.deepcopy(floor)
    repo.addEntity(new_floor)
    populated_building = repo.getBuilding()
    gateway.shutdown()
