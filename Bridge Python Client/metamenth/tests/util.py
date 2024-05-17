class JavaEnums:
    def __init__(self, gateway):
        self.gateway = gateway
        self.FloorType = gateway.jvm.com.middleware.enums.FloorType
        self.SpaceType = gateway.jvm.com.middleware.enums.OpenSpaceType
        self.RoomType = gateway.jvm.com.middleware.enums.RoomType
        self.MeterType = gateway.jvm.com.middleware.enums.MeterType
        self.MeasurementUnit = gateway.jvm.com.middleware.enums.MeasurementUnit
        self.MeterMeasureMode = gateway.jvm.com.middleware.enums.MeterMeasureMode
        self.BuildingType = gateway.jvm.com.middleware.enums.BuildingType
        self.SensorMeasure = gateway.jvm.com.middleware.enums.SensorMeasure
        self.SensorMeasureType = gateway.jvm.com.middleware.enums.SensorMeasureType
        self.SensorLogType = gateway.jvm.com.middleware.enums.SensorLogType
        self.OFFICE_TYPE = gateway.jvm.com.middleware.enums.OFFICE_TYPE
