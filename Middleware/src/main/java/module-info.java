module middleware {
	exports ca.concordia.encs.citydata.datastores;
	exports ca.concordia.encs.citydata.core.exceptions;
	exports ca.concordia.encs.citydata.producers;
	exports ca.concordia.encs.citydata.core;
	exports ca.concordia.encs.citydata.runners;
	exports ca.concordia.encs.citydata.operations;

	// requires transitive = to ensure that other modules reading your module also
	// read these dependencies
	requires transitive com.google.auth;
	requires transitive com.google.auth.oauth2;
	requires transitive java.net.http;
	requires transitive spring.beans;
	requires transitive spring.boot;
	requires transitive spring.boot.autoconfigure;
	requires transitive spring.context;
	requires transitive spring.core;
	requires transitive spring.web;
	requires transitive spring.webmvc;

	// Eclipse shows warnings for these modules because they do not have their own
	// module-info.java.
	requires firebase.admin;
	requires gson;
}