from metamenth.measure_instruments.interfaces.abstract_data_measure import AbstractDataMeasure


class TriggerHistory(AbstractDataMeasure):

    def __init__(self, trigger_type: str, value: float = None, timestamp: str = None):
        if value is None:
            value = 0.0

        super().__init__(value, timestamp)
        self._trigger_type = None

        self.setTriggerType(trigger_type)

    def setTriggerType(self, value: str):
        if value is None:
            raise ValueError('trigger type must be a string')
        self._trigger_type = value

    def getTriggerType(self) -> str:
        return self._trigger_type
