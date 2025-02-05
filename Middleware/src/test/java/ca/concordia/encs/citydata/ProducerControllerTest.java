package ca.concordia.encs.citydata;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.matchesPattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import ca.concordia.encs.citydata.core.AppConfig;

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSync() throws Exception {

// Create the JSON payload
        JsonObject payload = new JsonObject();
        payload.addProperty("use", "StringProducer");
        // Add "withParams" array
        JsonArray withParams = new JsonArray();
        JsonObject param1 = new JsonObject();
        param1.addProperty("name", "generationProcess");
        param1.addProperty("value", "random");
        withParams.add(param1);
        JsonObject param2 = new JsonObject();
        param2.addProperty("name", "stringLength");
        param2.addProperty("value", 10);
        withParams.add(param2);
        payload.add("withParams", withParams);
        // Add "apply" array
        JsonArray apply = new JsonArray();
        // First operation
        JsonObject operation1 = new JsonObject();
        operation1.addProperty("name", "StringReplaceOperation");
        JsonArray operation1Params = new JsonArray();
        JsonObject operation1Param1 = new JsonObject();
        operation1Param1.addProperty("name", "searchFor");
        operation1Param1.addProperty("value", "a");
        operation1Params.add(operation1Param1);
        JsonObject operation1Param2 = new JsonObject();
        operation1Param2.addProperty("name", "replaceBy");
        operation1Param2.addProperty("value", "b");
        operation1Params.add(operation1Param2);
        operation1.add("withParams", operation1Params);
        apply.add(operation1);
        // Second operation
        JsonObject operation2 = new JsonObject();
        operation2.addProperty("name", "StringReplaceOperation");
        JsonArray operation2Params = new JsonArray();
        JsonObject operation2Param1 = new JsonObject();
        operation2Param1.addProperty("name", "searchFor");
        operation2Param1.addProperty("value", "cxsffdf");
        operation2Params.add(operation2Param1);
        JsonObject operation2Param2 = new JsonObject();
        operation2Param2.addProperty("name", "replaceBy");
        operation2Param2.addProperty("value", "c");
        operation2Params.add(operation2Param2);
        operation2.add("withParams", operation2Params);
        apply.add(operation2);
        payload.add("apply", apply);
        // Convert the payload to a JSON string
        String jsonPayload = payload.toString();
        System.out.println("Sending JSON payload: " + jsonPayload); // Debug: Log the JSON payload
        mockMvc.perform(post("/apply/sync")
                        .contentType("application/json")
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(status().isOk()).andExpect(content().string(containsString("result")));
    }
//    @Test
//    public void testAsync() throws Exception{
//        mockMvc.perform(post("/apply/async")
//                        .contentType("application/json")
//                        .content(steps))
//                .andExpect(status().isOk())
//                .andExpect(content().string(matchesPattern("Hello! The runner .* is currently working on your request. Please make a GET request to /apply/async/.* to find out your request status.")));
//
//    }
}