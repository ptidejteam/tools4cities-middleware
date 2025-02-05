package ca.concordia.encs.citydata.producers;

import java.util.ArrayList;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.AbstractProducer;
import ca.concordia.encs.citydata.core.IProducer;

public class FirebaseProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	@Override
	public void fetch() {
		// TODO: re-add Rushin's Firebase code and Firebase dependencies
		this.result = new ArrayList<JsonObject>();
		this.applyOperation();
	}
}
