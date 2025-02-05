package ca.concordia.encs.citydata.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* This java class is to print all available producers and their characteristics */

@RestController
@RequestMapping("/producers")
public class ListProducerController {

	private static final String TARGET_PACKAGE = "ca/concordia/encs/citydata/producers"; // This is the path for the
																							// package

	@GetMapping("/list")
	public List<String> listProducers() throws IOException, ClassNotFoundException {
		List<String> producerDetails = new ArrayList<>();

		// Get the path to the package
		URL packageURL = Thread.currentThread().getContextClassLoader().getResource(TARGET_PACKAGE);

		if (packageURL == null) {
			return List.of("Package not found.");
		}

		// Scan for class files in the package directory
		File[] files = new File(packageURL.getFile()).listFiles((dir, name) -> name.endsWith(".class"));
		if (files != null) {
			for (File file : files) {
				// Remove .class extension
				String className = file.getName().substring(0, file.getName().length() - 6);
				// Load the class using reflection
				Class<?> clazz = Class.forName("ca.concordia.encs.citydata.producers." + className);

				// Prepare the producer details
				StringBuilder producerInfo = new StringBuilder();
				producerInfo.append("Producer: ").append(clazz.getSimpleName()).append("\n");

				// List Fields
				Field[] fields = clazz.getDeclaredFields();
				if (fields.length > 0) {
					producerInfo.append("Fields: ");
					for (Field field : fields) {
						producerInfo.append(field.getName()).append(" ");
					}
					producerInfo.append("\n");
				}

				// List Methods
				Method[] methods = clazz.getDeclaredMethods();
				if (methods.length > 0) {
					producerInfo.append("Methods: ");
					for (Method method : methods) {
						producerInfo.append(method.getName()).append(" ");
					}
					producerInfo.append("\n");
				}

				// Add the producer info to the list
				producerDetails.add(producerInfo.toString());
			}
		}

		return producerDetails.isEmpty() ? List.of("No producers found in package: " + TARGET_PACKAGE)
				: producerDetails;
	}
}