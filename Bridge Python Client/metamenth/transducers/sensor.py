from metamenth.transducers.interfaces.abstract_transducer import AbstractTransducer
from metamenth.misc.validate import Validate
from metamenth.datatypes.interfaces.abstract_range_measure import AbstractRangeMeasure
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure


class Sensor(AbstractTransducer):

    def __init__(self, name: str, measure: str, unit: str, measure_type: str,
                 data_frequency: float, gateway, current_value: float = None,
                 measure_range: AbstractRangeMeasure = None, sensor_log_type: str = None):
        """
        :param name: the unique name of a sensor
        :param measure: the phenomenom (e.g., temperature) this sensor measures
        :param unit: the measurement unit of the data being measured
        :param measure_type: the type of data measured by the sensor
        :param data_frequency: what interval is the data recorded
        :param current_value: the current value for the sensor
        :param gateway: py4j gateway
        """
        super().__init__(name, gateway)
        self._measure = None
        self._data_frequency = None
        self._unit = None
        self._current_value = current_value
        self._measure_type = None
        self._measure_range = measure_range
        self._sensor_log_type = sensor_log_type

        # Setting values using setters to perform validation
        self.setMeasure(measure)
        self.setDataFrequency(data_frequency)
        self.setUnit(unit)
        self.setMeasureType(measure_type)

        # validate sensor type and measurement
        if not Validate.validateSensorType(self.getMeasure(), self.getUnit()):
            raise ValueError("{0} sensor can not have {1} measurement unit".format(measure, unit))

    def getMeasure(self) -> str:
        return self._measure

    def setMeasure(self, value: str):
        if value is not None:
            self._measure = value
        else:
            raise ValueError("measure must be of type str")

    def getMeasureRange(self) -> AbstractRangeMeasure:
        return self._measure_range

    def setMeasureRange(self, value: AbstractRangeMeasure):
        if value is not None:
            self._measure_range = value
        else:
            raise ValueError("measure must be of type AbstractRangeMeasure")

    def getDataFrequency(self) -> float:
        return self._data_frequency

    def setDataFrequency(self, value: float):
        if value is not None:
            self._data_frequency = value
        else:
            raise ValueError("data_frequency must be float")

    def getUnit(self) -> str:
        return self._unit

    def setUnit(self, value: str):
        if value is not None:
            self._unit = value
        else:
            raise ValueError("unit must be of type str")

    def getCurrentValue(self) -> float:
        return self._current_value

    def setCurrentValue(self, value: float):
        self._current_value = value

    def getMeasureType(self) -> str:
        return self._measure_type

    def setMeasureType(self, value: str):
        if value is not None:
            self._measure_type = value
        else:
            raise ValueError("measure_type must be of type str")

    def getSensorLogType(self) -> str:
        return self._sensor_log_type

    def setSensorLogType(self, value: str):
        if value is not None:
            self._sensor_log_type = value
        else:
            raise ValueError("measure must be of type str")

    def setSetPoint(self, value: AbstractMeasure):
        self.setTransducerSetPoint(value, self._unit)

    def toString(self):
        return self.__str__()

    def __str__(self):
        sensor_data = "\n".join(str(data) for data in self._data)
        return (
            f"Sensor("
            f"{super().__str__()}, "
            f"UID: {self.getUID()}, "
            f"Name: {self.getName()}, "
            f"Measure: {self.getMeasure()}, "
            f"Measure Range: {self.getMeasureRange()}, "
            f"Data Frequency: {self.getDataFrequency()}, "
            f"Unit: {self.getUnit()}, "
            f"CurrentValue: {self.getCurrentValue()}, "
            f"Measure Type: {self.getMeasureType()}, "
            f"Log Type: {self.getSensorLogType()}, "
            f"Data Count: {len(sensor_data)}\n"
            f"Data: {sensor_data})"
        )

    class Java:
        implements = ['com.middleware.interfaces.metamenth.transducers.ISensor']