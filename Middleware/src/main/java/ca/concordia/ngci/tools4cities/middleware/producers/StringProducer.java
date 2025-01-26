package ca.concordia.ngci.tools4cities.middleware.producers;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class StringProducer {

	String generationProcess = "random";
	Integer stringLength = 10;

	public void setGenerationProcess(String generationProcess) {
		this.generationProcess = generationProcess;
	}
	
	public void setStringLength(Integer stringLength) {
		this.stringLength = stringLength;
	}

	public String fetch() {
        String result = "Here is a very exciting string.";
        System.out.println("Generating string...");

        if (this.stringLength > 0) {
            return result.substring(0, this.stringLength);
        }
		return result;
	}

}
