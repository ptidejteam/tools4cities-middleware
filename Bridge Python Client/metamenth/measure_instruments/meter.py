import uuid
from metamenth.enumerations.meter_accumulation_frequency import MeterAccumulationFrequency
from metamenth.measure_instruments.meter_measure import MeterMeasure
from metamenth.utils.search.structure_entity_search import StructureEntitySearch
from typing import Dict


class Meter:
    """
    A representation of a meter

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, meter_location: str, measurement_frequency: float,
                 measurement_unit: str, meter_type: str,
                 measure_mode: str, gateway, data_accumulated: bool = False,
                 accumulation_frequency: str = MeterAccumulationFrequency.NONE.value,
                 manufacturer: str = None):
        """
        Initializes a Meter instance.

        :param meter_location: The what3word location of the meter.
        :param manufacturer: The manufacturer of the meter.
        :param measurement_frequency: The measurement frequency of the meter.
        :param measurement_unit: The measurement unit of the meter data.
        :param meter_type: The type of the meter.
        :param measure_mode: the data measure mode: manual or automatic
        :param gateway: py4j gateway object
        :param data_accumulated: indicate whether the data is accummulate or not
        :param accumulation_frequency: the frequency at which data is accumulated
        """
        self._UID = str(uuid.uuid4())
        self._meter_location = meter_location
        self._manufacturer = None
        self._measurement_frequency = None
        self._meter_type = None
        self._measurement_unit = None
        self._measure_mode = None
        self._data_accumulated = data_accumulated
        self._accumulation_frequency = accumulation_frequency
        self._meter_measure: [MeterMeasure] = []
        self._structure_entity_search = StructureEntitySearch(gateway)

        # Apply validation
        self.setManufacturer(manufacturer)
        self.setMeasurementFrequency(measurement_frequency)
        self.setMeasurementUnit(measurement_unit)
        self.setMeterType(meter_type)
        self.setMeasureMode(measure_mode)
        self.setAccumulationFrequency(accumulation_frequency)

    def getMeterLocation(self):
        return self._meter_location

    def setMeterLocation(self, value):
        self._meter_location = value

    def getUID(self):
        return self._UID

    def getManufacturer(self) -> str:
        return self._manufacturer

    def setManufacturer(self, value: str):
        self._manufacturer = value

    def getMeasurementFrequency(self) -> float:
        return self._measurement_frequency

    def setMeasurementFrequency(self, value: float):
        if value is not None:
            self._measurement_frequency = value
        else:
            raise ValueError("measurement_frequency must be a float")

    def getMeasureMode(self) -> str:
        return self._measure_mode

    def setMeasureMode(self, value: str):
        if value is not None:
            self._measure_mode = value
        else:
            raise ValueError("measure_mode must be a float")

    def getDataAccumulated(self) -> bool:
        return self._data_accumulated

    def setDataAccumulated(self, value: bool):
        if value is not None:
            self._data_accumulated = value
        else:
            raise ValueError("data_accumulated must be a boolean")

    def getAccumulationFrequency(self) -> str:
        return self._accumulation_frequency

    def setAccumulationFrequency(self, value: str):
        if value is not None:
            if self.getDataAccumulated() and value is None:
                raise ValueError("accumulation_frequency must not be None")
            else:
                self._accumulation_frequency = value

    def getMeasurementUnit(self) -> str:
        return self._measurement_unit

    def setMeasurementUnit(self, value: str):
        if value is not None:
            self._measurement_unit = value
        else:
            raise ValueError("Measurement unit must be of type MeasurementUnit")

    def getMeterType(self) -> str:
        return self._meter_type

    def setMeterType(self, value: str):
        if value is not None:
            self._meter_type = value
        else:
            raise ValueError("Meter type must be of type MeterType")

    def addMeterMeasure(self, meter_measure: MeterMeasure):
        """
        Adds measurement reading for this meter
        :param meter_measure: the meter measurement
        :return:
        """
        self._meter_measure.append(meter_measure)

    def getMeterMeasures(self, search_terms: Dict = None) -> [MeterMeasure]:
        """
        Search meter recordings by attributes values
        :param search_terms: a dictionary of attributes and their values
        :return [MeterMeasure]:
        """
        return self._structure_entity_search.search(self._meter_measure, search_terms)

    def getMeterMeasureByDate(self, from_timestamp: str, to_timestamp: str = None) ->[MeterMeasure]:
        """
        searches meter recordings based on provided timestamp
        :param from_timestamp: the start timestamp
        :param to_timestamp: the end timestamp
        :return: [MeterMeasure]
        """
        return self._structure_entity_search.dateRangeSearch(self._meter_measure, from_timestamp, to_timestamp)

    def __eq__(self, other):
        # Meters are equal if they share the same UID
        if isinstance(other, Meter):
            # Check for equality based on the 'UID' attribute
            return self.getUID() == other.getUID() and self.getMeterLocation() == other.getMeterType() \
                   and self.getManufacturer() == other.getManufacturer() and self.getMeasureMode() == other.getMeasureMode() \
                   and self.getAccumulationFrequency() == other.getAccumulationFrequency()
        return False

    def toString(self):
        """
        :return: A formatted string representing the meter.
        """
        meter_details = (f"Meter (UID: {self.getUID()}, Location: {self.getMeterLocation()}, "
                         f"Manufacturer: {self.getManufacturer()}, Frequency: {self.getMeasurementFrequency()}, "
                         f"Unit: {self.getMeasurementUnit()}, Type: {self.getMeterType()}, "
                         f"Measure Mode: {self.getMeasureMode()}, Data Accumulated: {self.getDataAccumulated()}, "
                         f"Accumulation Frequency: {self.getAccumulationFrequency()})")

        return f"{meter_details}"

    class Java:
        implements = ['com.middleware.metamenth.interfaces.measureinstruments.IMeter']
