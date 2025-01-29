package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;

/**
*
* This Producer outputs both random or pre-defined strings. For test only.
*  
*/
public class StringProducer extends AbstractProducer<String> implements IProducer<String> {

	private String generationProcess = "random";
	private Integer stringLength = 10;
	
	public void setGenerationProcess(String generationProcess) {
		if (generationProcess != null) {
			this.generationProcess = generationProcess;
		}
	}
	
	public void setStringLength(Integer stringLength) {
		if (stringLength != null) {
			this.stringLength = stringLength;
		}
	}
	
	@Override
	public void fetch() {
		ArrayList<String> resultSet = new ArrayList<>();
		
		// if this is running for the first time, fetch
		// otherwise, just apply next operation on top of previous result
		if (this.result == null) {
				
			if (this.generationProcess.equalsIgnoreCase("random")) {
			   String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		       Random random = new Random();
		        while (resultSet.size() < this.stringLength) {
		            StringBuilder randomString = new StringBuilder();
		            for (int i = 0; i < 5; i++) {
		                int index = random.nextInt(characters.length());
		                randomString.append(characters.charAt(index));
		            }
		            resultSet.add(randomString.toString());
		        }
			} else if (this.generationProcess.equalsIgnoreCase("gabriel")) {
				 // added for test purposes
				 resultSet.add("g");
				 resultSet.add("a");
				 resultSet.add("b");
				 resultSet.add("r");
				 resultSet.add("i");
				 resultSet.add("e");
				 resultSet.add("l");
			} else {
				UUID uniqueId = UUID.randomUUID();
				resultSet.add(uniqueId.toString().substring(0, this.stringLength));
			}
			this.result = resultSet;
		} 
	   
        this.applyOperation();
	}

}
