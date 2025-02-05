package ca.concordia.encs.citydata;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.matchesPattern;

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
    private String steps = "{\n" +
            "    \"use\": \"StringProducer\",\n" +
            "    \"withParams\": [\n" +
            "        {\n" +
            "            \"name\": \"generationProcess\",\n" +
            "            \"value\": \"random\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"stringLength\",\n" +
            "            \"value\": 10\n" +
            "        }\n" +
            "    ],\n" +
            "    \"apply\": [\n" +
            "        {\n" +
            "            \"name\": \"StringReplaceOperation\",\n" +
            "            \"withParams\": [\n" +
            "                {\n" +
            "                    \"name\": \"searchFor\",\n" +
            "                    \"value\": \"a\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"replaceBy\",\n" +
            "                    \"value\": \"b\"\n" +
            "                }\n" +
            "            ]\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"StringReplaceOperation\",\n" +
            "            \"withParams\": [\n" +
            "                {\n" +
            "                    \"name\": \"searchFor\",\n" +
            "                    \"value\": \"x\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"name\": \"replaceBy\",\n" +
            "                    \"value\": \"c\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Test
    public void testSync() throws Exception {


        mockMvc.perform(post("/apply/sync")
                        .contentType("application/json")
                        .content(steps))
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern("\\[\\{\"result\":\"\\[.*\\]\"\\}\\]")));
    }
    @Test
    public void testAsync() throws Exception{
        mockMvc.perform(post("/apply/async")
                        .contentType("application/json")
                        .content(steps))
                .andExpect(status().isOk())
                .andExpect(content().string(matchesPattern("Hello! The runner .* is currently working on your request. Please make a GET request to /apply/async/.* to find out your request status.")));

    }
}