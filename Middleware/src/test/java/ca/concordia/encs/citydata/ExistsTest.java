package ca.concordia.encs.citydata;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.producers.OccupancyProducer;
import ca.concordia.encs.citydata.producers.RandomNumberProducer;

/* ExistsController tests
 * Author: [Your Name]
 * Date: 2025-02-26
 */

@SpringBootTest(classes = AppConfig.class)
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class ExistsTest {

    @Autowired
    private MockMvc mockMvc;
    
    // We'll use these test producer IDs in our tests
    private String randomProducerId;
    private String occupancyProducerId;
    
    // To ensure at least a producer exists
    @BeforeEach
    void setUp() throws Exception {
        // Clear the data store before each test
        InMemoryDataStore store = InMemoryDataStore.getInstance();
        store.truncate();
        
        randomProducerId = UUID.randomUUID().toString();
        occupancyProducerId = UUID.randomUUID().toString();
        
        // Create timestamp for today
        String timestamp = "2025-02-26T10:30:00Z";
        
        // Create RandomNumberProducer with valid data
        RandomNumberProducer randomProducer = new RandomNumberProducer();
        randomProducer.setListSize(5);
        randomProducer.setGenerationDelay(0);
        randomProducer.setMetadata("id", randomProducerId);
        randomProducer.setMetadata("timestamp", timestamp);
        randomProducer.fetch();
        
        // Create OccupancyProducer with valid data
        OccupancyProducer occupancyProducer = new OccupancyProducer();
        occupancyProducer.setListSize(5);
        occupancyProducer.setMetadata("id", occupancyProducerId);
        occupancyProducer.setMetadata("timestamp", timestamp);
        occupancyProducer.fetch();
        
        store.set(randomProducerId, randomProducer);
        store.set(occupancyProducerId, occupancyProducer);
    }

    // Creating a sample query payload based on producer ID
    private String createQueryPayload(String producerId) {
        JsonObject query = new JsonObject();
        query.addProperty("id", producerId);
        return query.toString();
    }
    
    // Test 1: Query is found
    @Test
    void testQueryExists() throws Exception {
        String jsonPayload = createQueryPayload(randomProducerId);
        
		mockMvc.perform(post("/exists/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonPayload))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Exists! Matching producers:")))
				.andExpect(content().string(containsString(randomProducerId)))
                .andExpect(content().string(containsString("timestamp")));
    }
    
    // Test 2: Query is not found (empty result)
    @Test
    void testQueryNotExists() throws Exception {
    	String queryPayload = "{\"id\": \"FakeProducerId\"}";

        // Perform the POST request and assert the response status and content
        mockMvc.perform(post("/exists/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(queryPayload))
               .andExpect(status().isNotFound()) 
               .andExpect(content().string("Does not exist."));
    }
    
    // Test 3: Query is broken JSON
    @Test
    void testBrokenJsonQuery() throws Exception {
        String jsonPayload = "{broken_json:}";
        
        mockMvc.perform(post("/exists/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isInternalServerError());
    }
    
    // Test 4: Query is found and after that you call /apply/async/{runnerId}
    @Test
    void testQueryExistsFollowedByAsync() throws Exception {
    	String runnerId = "";
		String jsonPayload = PayloadFactory.getExampleQuery("ckanProducerWithReplace");

		// do async request
		MvcResult asyncRequestResult = mockMvc
				.perform(post("/apply/async").contentType(MediaType.APPLICATION_JSON).content(jsonPayload))
				.andExpect(status().isOk()).andReturn();

		// Regular expression for extracting the runner ID
		String text = asyncRequestResult.getResponse().getContentAsString();
		String uuidRegex = "\\b[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\b";
		Pattern pattern = Pattern.compile(uuidRegex);
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			runnerId = matcher.group();
			break;
		}

		mockMvc.perform(get("/apply/async/" + runnerId)).andExpect(status().isOk())
				.andExpect(content().string(containsString("MiddlewareCollection")));
	
    }
}