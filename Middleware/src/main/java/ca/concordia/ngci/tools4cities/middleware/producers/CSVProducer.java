package ca.concordia.ngci.tools4cities.middleware.producers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class CSVProducer extends AbstractProducer<String> implements IProducer<String> {
	private String filePath;
	

	public CSVProducer(String filePath) {
		this.filePath = filePath;

	}

	@Override
	public void fetchData() throws FileNotFoundException {
		// Read data from CSV file
		final List<String> list = new ArrayList<String>();
		
		final Scanner sc = new Scanner(new File(this.filePath));
		sc.useDelimiter(",");

		while (sc.hasNext()) {
			list.add(sc.next());
		}
		sc.close();

		this.notifyObservers(list);
	}
}
