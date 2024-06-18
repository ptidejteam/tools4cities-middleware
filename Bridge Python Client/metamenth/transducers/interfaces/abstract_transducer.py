from abc import ABC
from uuid import uuid4
import sys
from typing import List
from typing import Union
from metamenth.measure_instruments.sensor_data import SensorData
from metamenth.measure_instruments.trigger_history import TriggerHistory
from metamenth.datatypes.interfaces.abstract_measure import AbstractMeasure
from typing import Dict
from metamenth.utils.search.structure_entity_search import StructureEntitySearch
from py4j.java_gateway import JavaGateway


class AbstractTransducer(ABC):
    def __init__(self,
                 name: str,
                 registry_id: str = None):
        """
        Describes a transducers (in a building)
        :param name: the unique name of the transducers
        :param registry_id: the registry id of the transducers
        """
        gateway = JavaGateway()
        self._UID = str(uuid4())
        self._name = None
        self._registry_id = registry_id
        self._set_point = None
        self._meta_data = gateway.jvm.java.util.HashMap()
        self._structure_entity_search = StructureEntitySearch()
        self._data = []

        self.setName(name)

    def getUID(self) -> str:
        return self._UID

    def getName(self) -> str:
        return self._name

    def setName(self, value: str):
        if value is None:
            raise ValueError('name is required')
        self._name = value

    def getRegistryId(self) -> str:
        return self._registry_id

    def setRegistryId(self, value: str):
        self._registry_id = value

    def getSetPoint(self) -> AbstractMeasure:
        return self._set_point

    def setTransducerSetPoint(self, setpoint: AbstractMeasure, measure: str):
        if setpoint is not None and measure is not None:
            if setpoint.getMeasurementUnit() != measure:
                raise ValueError('(Input) sensor measure: {} not matching set point measure: {}'
                                 .format(setpoint.getMeasurementUnit(), measure))
        self._set_point = setpoint

    def getMetaData(self):
        return self._meta_data

    def addData(self, data: Union[List[TriggerHistory], List[SensorData]]):
        if data is None:
            raise ValueError('data should be a list of SensorData or TriggerHistory')
        self._data.extend(data)

    def removeData(self, data: Union[TriggerHistory, SensorData]) -> bool:
        if data in self._data:
            self._data.remove(data)
            return True
        return False

    def addMetaData(self, key, value):
        """
        Adds meta data to transducers
        :param key: the key part of the metadata
        :param value: the value part of the metadata
        :return:
        """
        self._meta_data.put(key, value)

    def removeMetaData(self, key) -> bool:
        """
        removes meta data to transducers
        :param key: the key part of the metadata
        :return:
        """
        try:
            self._meta_data.remove(key)
            return True
        except KeyError as err:
            print(err, file=sys.stderr)
            return False

    def getData(self, search_terms: Dict = None) -> Union[List[SensorData], List[TriggerHistory]]:
        """
        Search data by attributes values
        :param search_terms: a dictionary of attributes and their values
        :return [SensorData|TriggerHistory]:
        """
        return self._structure_entity_search.search(self._data, search_terms)

    def getDataByDate(self, from_timestamp: str, to_timestamp: str = None) -> Union[List[SensorData],
    List[TriggerHistory]]:
        """
        searches transducer data based on provided timestamp
        :param from_timestamp: the start timestamp
        :param to_timestamp: the end timestamp
        :return: [SensorData|TriggerHistory]
        """
        return self._structure_entity_search.dateRangeSearch(self._data, from_timestamp, to_timestamp)

    def equals(self, other) -> bool:
        return self.__eq__(other)
    
    def __eq__(self, other):
        if isinstance(other, AbstractTransducer):
            # Check for equality based on the 'name' attribute
            return self.getName() == other.getName()
        return False

    def toString(self) -> str:
        return self.__str__()

    def __str__(self):
        return (f"Unit: {self.getUID()}, Name: {self.getName()}, Registry ID: {self.getRegistryId()}, "
                f"Set Point: {self.getSetPoint()}, "
                f"Metadata: {self.getMetaData()})")

    class Java:
        implements = ['ca.concordia.ngci.tools4cities.metamenth.interfaces.transducers.IAbstractTransducer']
