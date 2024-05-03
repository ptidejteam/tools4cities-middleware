from py4j.java_gateway import JavaGateway, CallbackServerParameters
from metamenth.measure_instruments.meter import Meter
from metamenth.enumerations.measurement_unit import MeasurementUnit
from metamenth.enumerations.meter_type import MeterType
from enumerations.meter_measure_mode import MeterMeasureMode
from metamenth.structure.room import Room
from metamenth.enumerations.room_type import RoomType
from metamenth.datatypes.measure import Measure
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure

if __name__ == "__main__":
    meter = Meter('hre.vrs.ies', 0.5, MeasurementUnit.KILOWATTS,
                  MeterType.ELECTRICITY, MeterMeasureMode.AUTOMATIC)
    measure = Measure(unit=MeasurementUnit.SQUARE_METERS, minimum=125)
    area = AbstractMeasure(measure)

    room = Room(area, name="STD 101", room_type=RoomType.STUDY_ROOM, location='hre.vrs.ies')
    room.setMeter(meter)
    gateway = JavaGateway(callback_server_parameters=CallbackServerParameters(),
                          python_server_entry_point=room)
