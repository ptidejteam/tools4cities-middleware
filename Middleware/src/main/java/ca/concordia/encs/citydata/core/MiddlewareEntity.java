package ca.concordia.encs.citydata.core;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 *
 * This is the most generic blueprint of what a "thing" in the middleware should
 * be. Producers, Operations, Runners and DataStores are middleware entities.
 * 
 * Refactoring made to allow MiddlewareEntity methods to receive and return the type java.util.UUID whenever possible.
 * Added 2 new methods getId() and getIdAsAString() to maintain compatibility where Strings are needed; and stored UUID
 * as UUIDs instead of Strings
 * 
 */
public class MiddlewareEntity {

	private HashMap<String, Object> metadata = new HashMap<>();

	public MiddlewareEntity() {
		UUID uniqueId = UUID.randomUUID();
		// Storing as UUID instead of uniqueId.toString()
		this.setMetadata("id", uniqueId);
	}
	
	public void setMetadata(String key, Object value) {
		metadata.put(key, value);
	}

	public Object getMetadata(String key) {
		return metadata.get(key);
	}
	
	// Here we get the entity's UUID 
    public UUID getId() {
        Object id = metadata.get("id");
        if (id instanceof UUID) {
            return (UUID) id;
        } else if (id instanceof String) {
            // To maintain compatibility with IDs stored as strings
            return UUID.fromString((String) id);
        }
        return null;
    }
    
    // Here we get the entity's ID as a String whenever it's needed
    public String getIdAsString() {
        UUID id = getId();
        return id != null ? id.toString() : "";
    }

	public Set<String> getMetadataKeySet() {
		return metadata.keySet();
	}

	public String getMetadataString(String key) {
        Object value = metadata.get(key);
        if (value != null) {
            return value.toString();
        }
        return "";
    }
		
}
