from datetime import datetime
from metamenth.enumerations.measurement_unit import MeasurementUnit
from metamenth.enumerations.sensor_measure import SensorMeasure


class Validate:
    """
    Has miscillineous methods for validation
    """

    @staticmethod
    def validateWhat3word(input_string: str) -> str:
        """
        Validates that a string is delimited by two "." with three words.

        :param input_string: The string to be validated.
        :return: the input string if valid else raises an error
        """

        if input_string is None or "":
            return ""
        # Split the string using "." as a delimiter
        parts = input_string.split(".")

        # Check if there are exactly three parts
        if len(parts) == 3 and all(part.strip() for part in parts):
            return input_string
        else:
            raise ValueError("Location should be a string of three words delimited with two periods.")

    @staticmethod
    def validateSolarHeatGainCoefficient(value: float) -> float:
        if value is not None:
            if 0 <= value <= 1:
                return value
            else:
                raise ValueError("Solar Heat Gain Coefficient must be a float between 0 and 1.")

    @staticmethod
    def parseDate(date_string):
        """
        Returns datetime in the format YYYY-MM-DD HH:MM:SS
        :param date_string: the data string
        :return:
        """
        formats = [
            '%Y-%m-%d %H:%M',
            '%Y-%m-%d',
            '%Y/%m/%d %H:%M:%S.%f',
            '%y/%m/%d %H:%M:%S.%f',
            '%Y/%m/%d %H:%M:%S',
            '%Y/%m/%d %H:%M',
            '%Y/%m/%d',
            '%m/%d/%Y %H:%M',
            '%Y-%m-%d %H:%M:%S.%f',
            '%Y-%m-%d %H:%M:%S'
            ]
        for fmt in formats:
            try:
                dt = datetime.strptime(date_string, fmt)
                return dt.replace(microsecond=0)  # Truncate milliseconds
            except ValueError as err:
                pass
        raise ValueError("No valid date format found")

    @staticmethod
    def validateSensorType(sensor_measure: str, unit: str) -> bool:
        """
        Validates the unit of measurement and the type of sensor
        e.g., a temperature sensor should have degree celsius as the measurement unit
        :param sensor_measure: the type of sensor e.g., temperature sensor
        :param unit: the unit of measurement for the type of sensor
        :return: true if the correct unit is used for a specific sensor else false
        Always returns true if sensor measure is other
        """
        if sensor_measure == SensorMeasure.TEMPERATURE.value:
            if unit == MeasurementUnit.DEGREE_CELSIUS.value:
                return True
        elif sensor_measure == SensorMeasure.PRESSURE.value:
            if unit == MeasurementUnit.PASCAL.value:
                return True
        elif sensor_measure == SensorMeasure.CARBON_DIOXIDE.value:
            if unit == MeasurementUnit.PARTS_PER_MILLION.value:
                return True
        elif sensor_measure == SensorMeasure.AIR_VOLUME.value:
            if unit in [MeasurementUnit.LITER.value, MeasurementUnit.CUBIC_FEET.value,
                        MeasurementUnit.CUBIC_CENTIMETER.value, MeasurementUnit.CUBIC_METER.value]:
                return True
        elif sensor_measure in [SensorMeasure.GAS_VELOCITY.value, SensorMeasure.LIQUID_VELOCITY.value]:
            if unit in [MeasurementUnit.METERS_PER_SECOND.value, MeasurementUnit.FEET_PER_SECOND.value]:
                return True
        elif sensor_measure == SensorMeasure.DAYLIGHT.value:
            if unit == MeasurementUnit.LUX.value:
                return True
        elif sensor_measure in [SensorMeasure.DIRECT_RADIATION.value, SensorMeasure.GLOBAL_RADIATION.value]:
            if unit == MeasurementUnit.WATTS_PER_METER_SQUARE.value:
                return True
        elif sensor_measure == SensorMeasure.LUMINANCE.value:
            if unit in [MeasurementUnit.CANDELA_PER_SQUARE_METER.value or MeasurementUnit.NITS.value]:
                return True
        elif sensor_measure == SensorMeasure.NOISE.value:
            if unit == MeasurementUnit.DECIBELS.value:
                return True
        elif sensor_measure == SensorMeasure.OCCUPANCY.value:
            if unit == MeasurementUnit.PRESENCE.value:
                return True
        elif sensor_measure == SensorMeasure.SMOKE.value:
            if unit == MeasurementUnit.MICROGRAM_PER_CUBIC_METER.value:
                return True
        elif sensor_measure == SensorMeasure.CURRENT.value:
            if unit == MeasurementUnit.AMPERE.value:
                return True
        elif sensor_measure == SensorMeasure.VOLTAGE.value:
            if unit == MeasurementUnit.VOLT.value:
                return True
        elif sensor_measure == SensorMeasure.HUMIDITY.value:
            if unit == MeasurementUnit.RELATIVE_HUMIDITY.value:
                return True
        elif sensor_measure == SensorMeasure.OTHER.value:
            return True
        return False

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.misc.IValidate']



