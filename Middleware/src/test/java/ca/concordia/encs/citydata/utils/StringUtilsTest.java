package ca.concordia.encs.citydata.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;

/**
 * This class tests various utility methods for string transformations and operations,
 * as well as methods for retrieving environment variables from system properties or a JSON file.
 *
 * Methods in this class are static and can be used without instantiating the class.
 *
 * Author: Rushin Makwana
 * Date: 2025-03-26
 */

import ca.concordia.encs.citydata.core.utils.StringUtils;

public class StringUtilsTest {

    @Test
    public void testCapitalizeWithLowerCase() {
        assertEquals("Rushin", StringUtils.capitalize("rushin"));
    }

    @Test
    public void testCapitalizeWithEmptyString() {
        assertEquals("", StringUtils.capitalize(""));
    }

    @Test
    public void testGetParamDescriptionsWithValidMethods() throws NoSuchMethodException {
        class TestClass {
            public void setName(String name) {}
            public void setAge(int age) {}
        }

        Method[] methods = TestClass.class.getDeclaredMethods();
        List<String> descriptions = StringUtils.getParamDescriptions(methods);
        System.out.println(descriptions);
        assertTrue(descriptions.contains("name (String)"));
        assertTrue(descriptions.contains("age (int)"));
    }

    @Test
    public void testGetParamDescriptionsExcludesInvalidMethods() throws NoSuchMethodException {
        class TestClass {
            public void setMetadata(String metadata) {}
        }

        Method[] methods = TestClass.class.getDeclaredMethods();
        List<String> descriptions = StringUtils.getParamDescriptions(methods);

        assertFalse(descriptions.contains("metadata (String)")); // setMetadata should be excluded
    }

    @Test
    public void fetchEnvVariableAndMatchWithConstant() {
        String expectedValue = "mongodb://localhost:27017/citydata";
        String actualValue = StringUtils.getEnvVariable("spring.data.mongodb.uri");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void fetchEnvVariableAndMatchWithAnotherConstant() {
        String expectedValue = null;
        String actualValue = StringUtils.getEnvVariable("HUB_APPLICATION_UUID");
        assertEquals(expectedValue, actualValue);
    }
}