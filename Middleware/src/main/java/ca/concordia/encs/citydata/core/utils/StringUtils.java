package ca.concordia.encs.citydata.core.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains functions to perform transformations on strings.
 * 
 * @Author: Rushin Makwana and Gabriel C. Ullmann
 * @Date: 28th Mar 2025
 */
public abstract class StringUtils {

	public static String capitalize(String str) {
		return str == null || str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static List<String> getParamDescriptions(Method[] methods) {
		List<String> descriptions = new ArrayList<>();
		for (Method method : methods) {
			Parameter[] params = method.getParameters();
			String methodName = method.getName();
			if (methodName != "setMetadata" && methodName.startsWith("set") && params.length > 0) {
				String paramName = method.getName().replace("set", "");
				paramName = paramName.substring(0, 1).toLowerCase() + paramName.substring(1, paramName.length());
				descriptions.add(paramName + " (" + params[0].getType().getSimpleName() + ")");
			}
		}
		return descriptions;
	}
}