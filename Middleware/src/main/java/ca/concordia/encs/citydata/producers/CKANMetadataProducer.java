package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IOperation;
import ca.concordia.encs.citydata.core.IProducer;
import ca.concordia.encs.citydata.core.IRunner;
import ca.concordia.encs.citydata.core.RequestOptions;

public class CKANMetadataProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	private String url;
	private String resourceId;
	private String datasetName;
	private JSONProducer jsonProducer;
	private IOperation<JsonObject> jsonProducerOperation;
	private IRunner runnerObserver;

	public void setUrl(String url) {
		if (url != null) {
			if (url.contains("http")) {
				this.url = url;
			} else {
				this.url = "https://" + url;
			}
		}
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	public void setOperation(IOperation operation) {
		this.jsonProducerOperation = operation;
	}

	@Override
	public void fetch() {
		if (this.url != null) {
			String actionUrl = this.url;
			if (this.resourceId != null) {
				actionUrl += "/action/resource_show?id=" + this.resourceId;
			} else if (this.datasetName != null) {
				actionUrl += "/action/package_show?id=" + this.datasetName;
			} else {
				// if no dataset or resource is specified, fetch the list of all datasets
				actionUrl += "/action/package_list";
			}

			// all CKAN metadata routes are GET routes
			RequestOptions requestOptions = new RequestOptions();
			requestOptions.method = "GET";

			// delegate to JSON producer
			this.jsonProducer = new JSONProducer(actionUrl, requestOptions);
			this.jsonProducer.operation = this.jsonProducerOperation;
			this.jsonProducer.addObserver(this.runnerObserver);
			this.jsonProducer.fetch();
		} else {
			JsonObject errorObject = new JsonObject();
			errorObject.addProperty("error",
					"No URL informed. Please use the 'url' parameter to specify a CKAN server URL.");
			this.result = new ArrayList<>();
			this.result.add(errorObject);
			super.addObserver(this.runnerObserver);
			super.operation = this.jsonProducerOperation;
			this.applyOperation();
		}
	}

	@Override
	public void addObserver(final IRunner aRunner) {
		this.runnerObserver = aRunner;
	}

}
