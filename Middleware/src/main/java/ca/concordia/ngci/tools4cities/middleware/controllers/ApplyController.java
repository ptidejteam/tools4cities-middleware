package ca.concordia.ngci.tools4cities.middleware.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
public class ApplyController {

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody String json) {
        try {
            JsonObject rootNode = JsonParser.parseString(json).getAsJsonObject();

            String producerName = getRequiredField(rootNode, "use").getAsString();
            JsonArray producerParams = getRequiredField(rootNode, "withParams").getAsJsonArray();
            JsonArray operationsNode = getRequiredField(rootNode, "apply").getAsJsonArray();

            Object producerInstance = instantiateClass("ca.concordia.ngci.tools4cities.middleware.producers." + producerName);
            setParameters(producerInstance, producerParams);

            for (JsonElement operationElement : operationsNode) {
                JsonObject operationNode = operationElement.getAsJsonObject();
                String operationName = getRequiredField(operationNode, "name").getAsString();
                JsonArray operationParams = getRequiredField(operationNode, "withParams").getAsJsonArray();

                Object operationInstance = instantiateClass("ca.concordia.ngci.tools4cities.middleware.operations." + operationName);
                setParameters(operationInstance, operationParams);
            }

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    private JsonElement getRequiredField(JsonObject jsonObject, String fieldName) {
        if (!jsonObject.has(fieldName)) {
            throw new IllegalArgumentException("Error: Missing '" + fieldName + "' field");
        }
        return jsonObject.get(fieldName);
    }

    private Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        return clazz.getDeclaredConstructor().newInstance();
    }

    private void setParameters(Object instance, JsonArray params) throws Exception {
        Class<?> clazz = instance.getClass();
        for (JsonElement paramElement : params) {
            JsonObject paramObject = paramElement.getAsJsonObject();
            String paramName = paramObject.get("name").getAsString();
            JsonElement paramValue = paramObject.get("value");
            Method setter = findSetterMethod(clazz, paramName, paramValue);
            setter.invoke(instance, convertValue(setter.getParameterTypes()[0], paramValue));
        }
    }

    private Method findSetterMethod(Class<?> clazz, String paramName, JsonElement paramValue) throws NoSuchMethodException {
        String methodName = "set" + capitalize(paramName);
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        throw new NoSuchMethodException("No suitable setter found for " + paramName);
    }

    private Object convertValue(Class<?> targetType, JsonElement value) {
        if (targetType == int.class || targetType == Integer.class) {
            return value.getAsInt();
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return value.getAsBoolean();
        } else if (targetType == double.class || targetType == Double.class) {
            return value.getAsDouble();
        } else {
            return value.getAsString();
        }
    }

    private String capitalize(String str) {
        return str == null || str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}