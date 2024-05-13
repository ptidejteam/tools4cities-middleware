from metamenth.measure_instruments.interfaces.abstract_data_measure import AbstractDataMeasure


class SensorData(AbstractDataMeasure):

    def __init__(self, value: float, timestamp: str = None):
        """
        :param value: The numerical value measured
        :param timestamp: the time of measurement
        """
        super().__init__(value, timestamp)