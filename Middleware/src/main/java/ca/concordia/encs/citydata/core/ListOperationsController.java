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

@RestController
@RequestMapping("/operations")
public class ListOperationsController {

	private static final String TARGET_PACKAGE = "ca/concordia/encs/citydata/operations"; // This is the path for the
																							// package

	@GetMapping("/list")
	public List<String> listOperations() throws IOException, ClassNotFoundException {
		List<String> operationDetails = new ArrayList<>();

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
				Class<?> clazz = Class.forName("ca.concordia.encs.citydata.operations." + className);

				// Prepare the operations details
				StringBuilder operationInfo = new StringBuilder();
				operationInfo.append("Operation: ").append(clazz.getSimpleName()).append("\n");

				// List Fields
				Field[] fields = clazz.getDeclaredFields();
				if (fields.length > 0) {
					operationInfo.append("Fields: ");
					for (Field field : fields) {
						operationInfo.append(field.getName()).append(" ");
					}
					operationInfo.append("\n");
				}

				// List Methods
				Method[] methods = clazz.getDeclaredMethods();
				if (methods.length > 0) {
					operationInfo.append("Methods: ");
					for (Method method : methods) {
						operationInfo.append(method.getName()).append(" ");
					}
					operationInfo.append("\n");
				}
				// Add the producer info to the list
				operationDetails.add(operationInfo.toString());
			}
		}

		return operationDetails.isEmpty() ? List.of("No Operations found in package: " + TARGET_PACKAGE)
				: operationDetails;

	}
}
