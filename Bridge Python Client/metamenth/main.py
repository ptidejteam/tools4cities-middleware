from py4j.java_gateway import JavaGateway, CallbackServerParameters, GatewayParameters
from metamenth.measure_instruments.meter import Meter
from metamenth.enumerations.measurement_unit import MeasurementUnit
from metamenth.enumerations.meter_type import MeterType
from enumerations.meter_measure_mode import MeterMeasureMode
from metamenth.structure.room import Room
from metamenth.structure.open_space import OpenSpace
from metamenth.enumerations.room_type import RoomType
from metamenth.datatypes.measure import Measure
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from metamenth.enumerations.open_space_type import OpenSpaceType
from metamenth.structure.floor import Floor
from metamenth.enumerations.floor_type import FloorType

if __name__ == "__main__":

    #gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(), python_server_entry_point=floor)

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

    meter = Meter('hre.vrs.ies', 0.5, MeasurementUnit.KILOWATTS.name(),
                  MeterType.ELECTRICITY.name(), MeterMeasureMode.AUTOMATIC.name(), False)
    measure = Measure(unit=MeasurementUnit.SQUARE_METERS.name(), minimum=125)
    area = AbstractMeasure(measure)

    room = Room(area, name="STD 101", room_type=RoomType.STUDY_ROOM.name(), location='hre.vrs.ies')
    hall = OpenSpace(name="Hall", area=area, space_type=SpaceType.HALL.name())
    room.setMeter(meter)
    floor = Floor(area=area, number=2, floor_type=FloorType.REGULAR.name(), room=room, open_space=hall)

    # provide unique object types which middleware could use to "construct" a building
    repo.addEntity(room)
    repo.addEntity(hall)
    repo.addEntity(floor)
   

    # TODO: Get basic building with a room floor and open space
