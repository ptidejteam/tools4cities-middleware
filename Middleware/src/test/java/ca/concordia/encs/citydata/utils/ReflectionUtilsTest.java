package ca.concordia.encs.citydata.utils;

import static org.junit.jupiter.api.Assertions.*;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.utils.ReflectionUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 *  This class contains test methods to validate the functionality of ReflectionUtils methods.
 *
 * Author: Rushin Makwana
 *Date: 2025-03-26
 *
 */

public class ReflectionUtilsTest {

    @Test
    public void testGetRequiredFieldWhenFieldExists() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Rushin");
        JsonElement result = ReflectionUtils.getRequiredField(jsonObject, "name");
        assertEquals("Rushin", result.getAsString());
    }
    @Test
    public void testGetRequiredFieldHandlesEmptyJsonObject() {
        JsonObject jsonObject = new JsonObject();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ReflectionUtils.getRequiredField(jsonObject, "anyField");
        });
        assertEquals("Error: Missing 'anyField' field", exception.getMessage());
    }

    @Test
    public void testInstantiateClassInvalidClassName() {
        Exception exception = assertThrows(ClassNotFoundException.class, () -> {
            ReflectionUtils.instantiateClass("non.existent.ClassName");
        });
        assertEquals("non.existent.ClassName", exception.getMessage());
    }

    @Test
    public void testInstantiateClassAbstractClass() {
        Exception exception = assertThrows(IllegalAccessException.class, () -> {
            ReflectionUtils.instantiateClass("java.util.AbstractList");
        });
        assertTrue(exception.getMessage().contains("java.util.AbstractList"));
    }

    @Test
    public void testSetParametersNull() {
        JsonArray params = new JsonArray();

        JsonObject param = new JsonObject();
        param.addProperty("name", "name");
        param.addProperty("value", "Rushin");
        params.add(param);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            ReflectionUtils.setParameters(null, params);
        });

        assertEquals("Cannot invoke \"Object.getClass()\" because \"instance\" is null", exception.getMessage());
    }
    @Test
    public void testFindSetterMethodReturnsValidSetter() throws Exception {
        class TestClass {
            public void setName(String name) {}
        }

        Method method = ReflectionUtils.findSetterMethod(TestClass.class, "name", new JsonObject());
        assertNotNull(method);
        assertEquals("setName", method.getName());
    }
    @Test
    public void testFindSetterMethodThrowsException() {
        class TestClass {
            public void setAge(int age) {}
        }

        Exception exception = assertThrows(MiddlewareException.NoSuitableSetterException.class, () -> {
            ReflectionUtils.findSetterMethod(TestClass.class, "name", new JsonObject());
        });

        assertEquals("No suitable setter found for name", exception.getMessage());
    }
    @Test
    public void testConvertValueBooleanType() {
        JsonElement value = new JsonObject();
        value.getAsJsonObject().addProperty("key", true);
        assertEquals(true, ReflectionUtils.convertValue(boolean.class, value.getAsJsonObject().get("key")));
    }

    @Test
    public void testConvertValueDoubleType() {
        JsonElement value = new JsonObject();
        value.getAsJsonObject().addProperty("key", 3.14);
        assertEquals(3.14, ReflectionUtils.convertValue(double.class, value.getAsJsonObject().get("key")));
    }

}
