from py4j.java_gateway import JavaGateway, GatewayParameters

gateway = JavaGateway(gateway_parameters=GatewayParameters(auto_convert=True))
building = gateway.entry_point.getLBBuilding()

# Get the building created by the Java BuildingManager
floor = building.getFloors({})[0]
print(floor.getRoomByName("Room 001"))
