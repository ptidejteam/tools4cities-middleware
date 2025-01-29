package com.concordia;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class EnergyConsumptionTest {

    @Autowired
    private MockMvc mockMvc; // For testing REST endpoints

    @Test
    public void testEndpoint() throws Exception {
        final String queryParams = "startDatetime=2021-01-01T13:00:00.000Z&endDatetime=2021-01-02T00:45:00.000Z&postalCode=H2Y2E4";

        mockMvc.perform(get("/consumers/energyConsumption?" + queryParams))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("timestamp"))); // Verify response contains "timestamp"
    }
}
