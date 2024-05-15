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

if __name__ == "__main__":

    gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                          gateway_parameters=GatewayParameters(auto_convert=True))

    repo = gateway.entry_point.getMetamenthRepository()

    # Get Java Enums
    FloorType = gateway.jvm.com.middleware.enums.FloorType
    SpaceType = gateway.jvm.com.middleware.enums.OpenSpaceType
    RoomType = gateway.jvm.com.middleware.enums.RoomType
    MeterType = gateway.jvm.com.middleware.enums.MeterType
    MeasurementUnit = gateway.jvm.com.middleware.enums.MeasurementUnit
    MeterMeasureMode = gateway.jvm.com.middleware.enums.MeterMeasureMode
    BuildingType = gateway.jvm.com.middleware.enums.BuildingType
    SensorMeasure = gateway.jvm.com.middleware.enums.SensorMeasure
    SensorMeasureType = gateway.jvm.com.middleware.enums.SensorMeasureType
    SensorLogType = gateway.jvm.com.middleware.enums.SensorLogType

    meter = Meter('hre.vrs.ies', 0.5, MeasurementUnit.KILOWATTS.getValue(),
                  MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue(), False)
    measure = Measure(unit=MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
    area = BinaryMeasure(measure)

    sensor = Sensor(name='TMP.01', measure=SensorMeasure.TEMPERATURE.getValue(), data_frequency=90,
                    unit=MeasurementUnit.DEGREE_CELSIUS.getValue(),
                    measure_type=SensorMeasureType.THERMO_COUPLE_TYPE_A.getValue(),
                    sensor_log_type=SensorLogType.POLLING.getValue())

    # add some data to the sensor
    sensor.addData([SensorData(14), SensorData(15), SensorData(16.8), SensorData(25.8), SensorData(10.19)])

    room = Room(area, name="STD 101", room_type=RoomType.STUDY_ROOM.getValue(), location='hre.vrs.ies')
    hall = OpenSpace(name="Hall", area=area, space_type=SpaceType.HALL.getValue())
    room.setMeter(meter)
    floor = Floor(area=area, number=2, floor_type=FloorType.REGULAR.getValue(), room=room, open_space=hall)

    height_measure = Measure(unit=MeasurementUnit.METERS.getValue(), minimum=50)
    height = BinaryMeasure(height_measure)
    area.setValue(1100)
    address = Address(city='Montreal', street='3965 Rue Sherbrooke', zip_code='H1N 1E3', state='QC', country='Canada',
                      geocoordinate=Point(lat=4.8392838293, lon=-1.389883929))
    building = Building(construction_year=2024, height=height, floor_area=area,
                        building_type=BuildingType.COMMERCIAL.getValue(), address=address, floor=floor, gateway=gateway)

    # provide unique object types which middleware could use to "construct" a building
    repo.addEntity(building)
    new_room = copy.deepcopy(room)
    repo.addEntity(new_room)
    repo.addEntity(sensor)
    new_floor = copy.deepcopy(floor)
    repo.addEntity(new_floor)
    populated_building = repo.getBuilding()
    gateway.shutdown()

