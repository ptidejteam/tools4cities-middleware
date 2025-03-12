package ca.concordia.encs.citydata.core.implementations;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 *
 * This is the most generic blueprint of what a "thing" in the middleware should
 * be. Producers, Operations, Runners and DataStores are middleware entities.
 * 
 */
public class MiddlewareEntity {

	private HashMap<String, Object> metadata = new HashMap<>();

	public MiddlewareEntity() {
		UUID uniqueId = UUID.randomUUID();
		this.setMetadata("id", uniqueId.toString());
	}

	public void setMetadata(String key, Object value) {
		metadata.put(key, value);
	}

	public Object getMetadata(String key) {
		return metadata.get(key);
	}

	public Set<String> getMetadataKeySet() {
		return metadata.keySet();
	}

	public String getMetadataString(String key) {
		Object value = metadata.get(key);
		if (value != null) {
			return metadata.get(key).toString();
		}
		return "";
	}
}
