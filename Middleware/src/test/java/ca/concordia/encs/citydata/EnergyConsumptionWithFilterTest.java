package ca.concordia.encs.citydata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ca.concordia.encs.citydata.core.configs.AppConfig;
import ca.concordia.encs.citydata.producers.EnergyConsumptionProducer;
import ca.concordia.encs.citydata.operations.StringFilterOperation;

/***
 * This test validates the energy consumption data filtering through the API endpoint and also directly through 
 * the producer component.
 * 
 * @author Minette Zongo M.
 * @date 2025-04-29
 */


@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class EnergyConsumptionWithFilterTest {
    @Autowired
    private MockMvc mockMvc;

    private EnergyConsumptionProducer energyConsumptionProducer;

    @BeforeEach
    void setUp() {
        energyConsumptionProducer = new EnergyConsumptionProducer();
    }
    
    @Test
    public void testEnergyConsumptionWithTimeFilter() throws Exception {
        String jsonPayload = PayloadFactory.getExampleQuery("energyConsumptionWithFilter");

        mockMvc.perform(post("/apply/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("result")));

        // testing the producer component directly
        energyConsumptionProducer.setCity("montreal");
        energyConsumptionProducer.fetch();

        // collecting results
        ArrayList<String> result = energyConsumptionProducer.getResult();
        assertNotNull(result, "Result should not be null");
        assertThat(result).isNotEmpty();

        // Manually applying the time filter operation
        StringFilterOperation filterOperation = new StringFilterOperation();
        filterOperation.setFilterBy("09:45:00");

        ArrayList<String> filteredResult = filterOperation.apply(result);
        assertThat(filteredResult).isNotEmpty();
        assertThat(filteredResult).allMatch(entry -> entry.contains("09:45:00"));
    }
}