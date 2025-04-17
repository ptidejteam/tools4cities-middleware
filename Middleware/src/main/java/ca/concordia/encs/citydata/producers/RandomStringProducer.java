package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;

/**
 *
 * This Producer outputs both random or pre-defined strings. For test only.
 * 
 */
public class RandomStringProducer extends AbstractProducer<String> implements IProducer<String> {

	//private String generationProcess = "random";
//	private String inputString = "";
	//private Integer stringLength = 10;
//
	//public void setGenerationProcess(String generationProcess) {
	//	if (generationProcess != null) {
		//	this.generationProcess = generationProcess;
		//}
	//}
	
//	public void setStringLength(Integer stringLength) {
//		if (stringLength != null) {
//			this.stringLength = stringLength;
//		}
//	}
//
//	public void setInputString(String inputString) {
//		this.inputString = inputString;
//	}
//
//	@Override
//	public void fetch() {
//		ArrayList<String> resultSet = new ArrayList<>();
//
//		// if this is running for the first time, fetch
//		// otherwise, just apply next operation on top of previous result
//		if (this.result == null) {
//
//			if (this.generationProcess.equalsIgnoreCase("processInput")) {
//				resultSet.add(this.inputString);
//			} else if (this.generationProcess.equalsIgnoreCase("random")) {
//				String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//				Random random = new Random();
//				while (resultSet.size() == 0) {
//					StringBuilder randomString = new StringBuilder();
//					for (int i = 0; i < this.stringLength; i++) {
//						int index = random.nextInt(characters.length());
//						randomString.append(characters.charAt(index));
//					}
//					resultSet.add(randomString.toString());
//				}
//			} else {
//				UUID uniqueId = UUID.randomUUID();
//				resultSet.add(uniqueId.toString().substring(0, this.stringLength));
//			}
//			this.result = resultSet;
//		}
//
//		this.applyOperation();
//	}

	private int stringLength = 10; // default
    private String generationProcess;
    private String inputString; 

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public void setStringLength(Integer stringLength) {
        if (stringLength != null) {
            this.stringLength = stringLength;
        }
    }

    public void setGenerationProcess(String generationProcess) {
        this.generationProcess = generationProcess;
    }

    @Override
    public void setOperation(IOperation operation) {
        this.operation = operation;
    }

    @Override
    public void fetch() {
        ArrayList<String> resultSet = new ArrayList<>();
        if (this.result == null) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            Random random = new Random();
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < this.stringLength; i++) {
                int index = random.nextInt(characters.length());
                randomString.append(characters.charAt(index));
            }
            resultSet.add(randomString.toString());
            this.result = resultSet;
        }
        this.applyOperation();
    }
}
