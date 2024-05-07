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

    meter = Meter('hre.vrs.ies', 0.5, MeasurementUnit.KILOWATTS.getValue(),
                  MeterType.ELECTRICITY.getValue(), MeterMeasureMode.AUTOMATIC.getValue(), False)
    measure = Measure(unit=MeasurementUnit.SQUARE_METERS.getValue(), minimum=125)
    area = BinaryMeasure(measure)

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
    new_floor = copy.deepcopy(floor)
    repo.addEntity(new_floor)
    populated_building = repo.getBuilding()
    print(populated_building)
    gateway.shutdown()

