package ca.concordia.encs.citydata;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AppConfig;

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class ExistsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    private String producerId;

    @BeforeEach
    void setUp() throws Exception {
        // Create a test producer before each test
        JsonObject producerConfig = new JsonObject();
        producerConfig.addProperty("type", "energy_consumption");
        producerConfig.addProperty("city", "montreal");
        producerConfig.addProperty("use", "ca.concordia.encs.citydata.producers.EnergyConsumptionProducer");

        MvcResult result = mockMvc.perform(post("/producers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(producerConfig.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        producerId = response.substring(response.indexOf("ID: ") + 4, response.indexOf(","));
    }

    @Test
    public void whenCheckExistingProducer_thenReturnSuccess() throws Exception {
        mockMvc.perform(get("/exists/" + producerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Producer exists")));
    }

    @Test
    public void whenQueryMatchesProducer_thenReturnSuccess() throws Exception {
        JsonObject query = new JsonObject();
        query.addProperty("type", "energy_consumption");
        query.addProperty("city", "montreal");

        mockMvc.perform(post("/exists/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(query.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Exact query already exists")));
    }

    @Test
    public void whenQueryDoesNotMatch_thenReturnNotFound() throws Exception {
        JsonObject query = new JsonObject();
        query.addProperty("type", "energy_consumption");
        query.addProperty("city", "toronto");

        mockMvc.perform(post("/exists/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(query.toString()))
                .andExpect(status().is(404))
                .andExpect(content().string(containsString("No matching producer found")));
    }

    @Test
    public void whenGetAllProducers_thenReturnList() throws Exception {
        mockMvc.perform(get("/exists/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("producers")));
    }
}