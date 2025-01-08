from py4j.java_gateway import JavaGateway, GatewayParameters

gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True))
building = gateway.entry_point.getLBBuilding()
weather_station = building.getWeatherStation('LB WS')
weather_data = weather_station.getWeatherData({})
print(weather_data)
meter = building.getMeters({'MeterType': 'Electricity'})[0]
meter_data = meter.getMeterMeasures({})
floor = building.getFloorByNumber(1)
room = floor.getRoomByName('Room 001')
sensor = room.getTransducer('TMP 01')
sensor_data = sensor.getData({})
print(sensor_data)



