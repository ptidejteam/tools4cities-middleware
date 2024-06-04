from py4j.java_gateway import JavaGateway

gateway = JavaGateway()
building = gateway.entry_point.getLBBuilding()

# Get the building created by the Java BuildingManager
print(building)
