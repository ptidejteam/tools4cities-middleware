package ca.concordia.ngci.tools4cities.middleware.producer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ca.concordia.ngci.tools4cities.middleware.consumer.IConsumer;

public class CSVProducer implements IProducer<List<String>> {
	@Override
	public List<String> fetchData() throws FileNotFoundException {
		// Read data from CSV file
		final List<String> list = new ArrayList<String>();
		final Scanner sc = new Scanner(new File("C:\\Users\\si_ejaz\\Desktop\\Data\\Data.csv"));
		sc.useDelimiter(",");

		while (sc.hasNext()) {
			list.add(sc.next());
		}
		sc.close();
		return list;
	}

	@Override
	public void addObserver(IConsumer<List<String>> aConsumer) {
		// TODO Auto-generated method stub
		
	}
}
