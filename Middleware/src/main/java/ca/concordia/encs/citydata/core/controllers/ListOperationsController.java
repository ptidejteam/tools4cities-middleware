package ca.concordia.encs.citydata.core.controllers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.concordia.encs.citydata.core.contracts.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* This java class is to print all available operations and their characteristics 
 * Author: Sikandar Ejaz
 * Date: 2-6-2025
 */

@RestController
@RequestMapping("/operations")
public class ListOperationsController {


	@GetMapping("/list")
	public List<Map<String, String>> listOperations() throws IOException, ClassNotFoundException {
		List<Map<String, String>> operationDetailsList = new ArrayList<>();

		// Get the path to the package
		URL packageURL = Thread.currentThread().getContextClassLoader().getResource(Constants.OPERATION_ROOT_PACKAGE);

		if (packageURL == null) {
			return List.of(Map.of("error", "Package not found."));
		}

		// Scan for class files in the package directory
		File[] files = new File(packageURL.getFile()).listFiles((dir, name) -> name.endsWith(".class"));

		if (files != null) {
			for (File file : files) {
				// Remove .class extension
				String className = file.getName().substring(0, file.getName().length() - 6);

				// Load the class using reflection
				Class<?> clazz = Class.forName("ca.concordia.encs.citydata.operations." + className);

				// Map to hold operation details
				Map<String, String> operationDetails = new HashMap<>();

				// Set class name
				operationDetails.put("name", clazz.getName());

				// Collect fields and method signatures
				List<String> paramList = new ArrayList<>();

				// List fields
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					paramList.add(field.getName() + " (" + field.getType().getSimpleName() + ")");
				}

				operationDetails.put("params", String.join(", ", paramList));

				operationDetailsList.add(operationDetails);
			}
		}

		return operationDetailsList.isEmpty()
				? List.of(Map.of("message", "No operations found in package: " + Constants.OPERATION_ROOT_PACKAGE))
				: operationDetailsList;
	}
}
