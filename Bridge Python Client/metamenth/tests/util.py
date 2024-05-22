class JavaEnums:
    def __init__(self, gateway):
        self.gateway = gateway
        self.FloorType = gateway.jvm.com.middleware.metamenth.enums.FloorType
        self.SpaceType = gateway.jvm.com.middleware.metamenth.enums.OpenSpaceType
        self.RoomType = gateway.jvm.com.middleware.metamenth.enums.RoomType
        self.MeterType = gateway.jvm.com.middleware.metamenth.enums.MeterType
        self.MeasurementUnit = gateway.jvm.com.middleware.metamenth.enums.MeasurementUnit
        self.MeterMeasureMode = gateway.jvm.com.middleware.metamenth.enums.MeterMeasureMode
        self.BuildingType = gateway.jvm.com.middleware.metamenth.enums.BuildingType
        self.SensorMeasure = gateway.jvm.com.middleware.metamenth.enums.SensorMeasure
        self.SensorMeasureType = gateway.jvm.com.middleware.metamenth.enums.SensorMeasureType
        self.SensorLogType = gateway.jvm.com.middleware.metamenth.enums.SensorLogType
        self.OfficeType = gateway.jvm.com.middleware.metamenth.enums.OFFICE_TYPE
        self.CoverType = gateway.jvm.com.middleware.metamenth.enums.CoverType
        self.MaterialType = gateway.jvm.com.middleware.metamenth.enums.MaterialType
