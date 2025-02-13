package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.AbstractRunner;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.RequestOptions;
import ca.concordia.encs.citydata.datastores.DiskDatastore;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.runners.SingleStepRunner;

/**
 * This producer can connect to a CKAN instance and fetch a resource.
 */
public class CKANProducer extends AbstractProducer<String> implements IProducer<String> {

	private String url;
	private String resourceId;
	private DiskDatastore diskStore = DiskDatastore.getInstance();
	private ArrayList<String> intermediateResult = new ArrayList<>();

	public void setUrl(String url) {
		if (url != null) {
			if (url.contains("http")) {
				this.url = url;
			} else {
				this.url = "https://" + url;
			}
		}
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	private ArrayList<JsonObject> getMetadataObject(AbstractRunner aRunner) {
		InMemoryDataStore memoryStore = InMemoryDataStore.getInstance();
		String runnerId = aRunner.getMetadata("id").toString();
		IProducer<?> storeResult = memoryStore.get(runnerId);
		if (storeResult != null) {
			return (ArrayList<JsonObject>) memoryStore.get(runnerId).getResult();
		}
		return new ArrayList<>();
	}

	private JsonObject getResourceAttributes(ArrayList<JsonObject> metadataObject) {
		JsonObject metadataResults = metadataObject.size() > 0 ? metadataObject.get(0).get("result").getAsJsonObject()
				: null;

		JsonObject attributesObject = new JsonObject();
		attributesObject.addProperty("sizeInMb", metadataResults.get("size").getAsInt() / 1000000);
		attributesObject.addProperty("mimetype", metadataResults.get("mimetype").getAsString());
		attributesObject.addProperty("url", metadataResults.get("url").getAsString());

		return attributesObject;
	}

	private boolean isFileSupported(String mimetype) {
		List<String> supportedFormats = List.of("csv", "json", "xml", "txt", "text", "xls");
		for (String supportedFormat : supportedFormats) {
			if (mimetype.contains(supportedFormat)) {
				return true;
			}
		}
		return false;
	}

	private byte[] fetchFromCkan() {
		try {
			String metadataProducerName = "ca.concordia.encs.citydata.producers.CKANMetadataProducer";
			JsonArray metadataProducerParams = JsonParser.parseString("[ {\"name\": \"url\", \"value\": \"" + this.url
					+ "\"},\n" + "{\"name\": \"resourceId\", \"value\": \"" + this.resourceId + "\"} ]")
					.getAsJsonArray();

			SingleStepRunner deckard = new SingleStepRunner(metadataProducerName, metadataProducerParams);
			Thread runnerTask = new Thread() {
				public void run() {
					try {
						deckard.runSteps();
						while (!deckard.isDone()) {
							System.out.println("Busy waiting!");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
			};
			runnerTask.start();
			runnerTask.join();

			ArrayList<JsonObject> metadataObject = getMetadataObject(deckard);
			JsonObject resourceAttributes = getResourceAttributes(metadataObject);
			String resourceUrl = resourceAttributes.get("url").getAsString();
			String mimetype = resourceAttributes.get("mimetype").getAsString();
			Integer size = resourceAttributes.get("sizeInMb").getAsInt();

			// if the file is supported, download it and save to disk
			if (isFileSupported(mimetype)) {
				if (size > 800) {
					intermediateResult
							.add("Sorry, files larger than 800MB are not currently supported by this producer. "
									+ "If you wish to download the resource, please open this link in your browser: "
									+ resourceAttributes.get("url") + " .");
				}
				RequestOptions requestOptions = new RequestOptions();
				requestOptions.method = "GET";
				this.filePath = resourceUrl;
				this.fileOptions = requestOptions;
				return this.fetchFromPath();
			} else {
				intermediateResult.add("Sorry, the " + mimetype + " type is not currently supported by this producer. "
						+ "If you wish to download the resource, please open this link in your browser: " + resourceUrl
						+ " .");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	@Override
	public void fetch() {

		if (this.resourceId != null) {

			// before attempting to fetch, check if a file with this resource ID already
			// exists in the disk
			byte[] fileOnDisk = diskStore.get(this.resourceId);

			// if not, fetch from CKAN and save on disk
			if (fileOnDisk == null) {
				byte[] fileFromCkan = fetchFromCkan();
				diskStore.set(this.resourceId, fileFromCkan);
				intermediateResult.add(new String(fileFromCkan));
			} else {
				intermediateResult.add(new String(fileOnDisk));
			}
			this.result = this.intermediateResult;
			this.applyOperation();
		}

	}

}
