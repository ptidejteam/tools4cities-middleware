class JavaEnums:
    def __init__(self, gateway):
        self.gateway = gateway
        self.FloorType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.FloorType
        self.SpaceType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.OpenSpaceType
        self.RoomType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.RoomType
        self.MeterType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.MeterType
        self.MeasurementUnit = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.MeasurementUnit
        self.MeterMeasureMode = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.MeterMeasureMode
        self.BuildingType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.BuildingType
        self.SensorMeasure = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasure
        self.SensorMeasureType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.SensorMeasureType
        self.SensorLogType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.SensorLogType
        self.OfficeType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.OFFICE_TYPE
        self.CoverType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.CoverType
        self.ZoneType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.ZoneType
        self.HvacType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.HvacType
        self.MaterialType = gateway.jvm.ca.concordia.ngci.tools4cities.metamenth.enums.MaterialType
