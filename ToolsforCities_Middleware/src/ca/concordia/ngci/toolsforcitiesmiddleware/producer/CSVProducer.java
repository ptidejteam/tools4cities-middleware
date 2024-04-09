package ca.concordia.ngci.toolsforcitiesmiddleware.producer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
}
