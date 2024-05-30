from metamenth.measure_instruments.interfaces.abstract_data_measure import AbstractDataMeasure


class MeterMeasure(AbstractDataMeasure):
    """
    This class represents the reading values of a meter in a building.
    The unit of measurement depends on the phenomenon measured by a meter

    Author: Peter Yefi
    Email: peteryefi@gmail.com
    """

    def __init__(self, value: float, timestamp: str = None, measurement_type: str = None):
        """
        :param value: The numerical value measured
        :param timestamp: the time of measurement
        :param measurement_type: the type of the measurment, e.g., electricity consumption

        """
        super().__init__(value, timestamp, measurement_type)

    class Java:
        implements = ['com.middleware.metamenth.interfaces.measureinstruments.IMeterMeasure']
