package ca.concordia.ngci.tools4cities.metamenth;


import ca.concordia.ngci.tools4cities.metamenth.interfaces.IPythonEntryPoint;
import ca.concordia.ngci.tools4cities.metamenth.interfaces.structure.IBuilding;
import py4j.GatewayServer;


/**
 * A Java client that creates Python objects by 
  * calling the appropriate methods on a Python entry point object
  * provided when connection is established with a Python server. 
  * PythonObjectCreate passed to the connection enable python to access
  * the appropriate Java objects
 * @author Peter Yefi
 */
public class PythonEntryServer {
    
    private PythonObjectCreator pythonObjectCreator = new PythonObjectCreator();
    private GatewayServer gatewayServer;
    private IBuilding building;

    public PythonEntryServer(){
      this.gatewayServer = new GatewayServer(this);
      this.gatewayServer.start();
    }
    
    /**
     * Creates the LB building by access the relevant MetamEnTh class
     * and Middleare operations and productions
     * @param pythonEntryPoint, python object to provides access to MetamEnTh classes
     */
    public void createLBBuilding(IPythonEntryPoint pythonEntryPoint){
      building = pythonObjectCreator.createLBBuilding(pythonEntryPoint);
    }

    public IBuilding getLBBuilding(){
      return building;
    }

    


    public static void main(String[] args) {
    
        PythonEntryServer pythonEntryServer = new PythonEntryServer();
        IPythonEntryPoint pythonEntryPoint = (IPythonEntryPoint) pythonEntryServer.gatewayServer.getPythonServerEntryPoint(new Class[]{ IPythonEntryPoint.class });
        pythonEntryServer.createLBBuilding(pythonEntryPoint);
        System.out.println("Server is running!!!");
  }
}
