package ca.concordia.encs.citydata.core.implementations;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import ca.concordia.encs.citydata.core.contracts.IEntity;

/**
 *
 * This is the most generic blueprint of what a "thing" in the CityData should
 * be. Producers, Operations, Runners and DataStores are CityData entities.
 * 
 * @author Gabriel C. Ullmann
 * @date 2025-04-23
 */
public abstract class AbstractEntity implements IEntity {

	private HashMap<String, Object> metadata = new HashMap<>();

	public AbstractEntity() {
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
