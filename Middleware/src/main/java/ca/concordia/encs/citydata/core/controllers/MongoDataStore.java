package ca.concordia.encs.citydata.core.controllers;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import ca.concordia.encs.citydata.core.ProducerUsageData;
import ca.concordia.encs.citydata.core.contracts.IDataStore;
import ca.concordia.encs.citydata.core.implementations.MiddlewareEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/***
 * This class is responsible for storing and retrieving data from MongoDB. Data
 * remains persists even after the application stops.
 * 
 * @Author: Rushin Makwana
 * @Date: 26/2/2024
 */

@Component
public class MongoDataStore extends MiddlewareEntity implements IDataStore<ProducerUsageData> {

	private final MongoTemplate mongoTemplate;

	@Autowired
	public MongoDataStore(@Autowired(required = false) MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void set(String key, ProducerUsageData value) {
		if (mongoTemplate != null) {
			mongoTemplate.save(value, key);
		}
	}

	@Override
	public ProducerUsageData get(String key) {
		if (mongoTemplate != null) {
			return mongoTemplate.findById(key, ProducerUsageData.class);
		}
		return null;
	}

	@Override
	public void delete(String key) {
		if (mongoTemplate != null) {
			mongoTemplate.remove(query(where("_id").is(key)), ProducerUsageData.class);
		}
	}

	public void save(ProducerUsageData callInfo) {
		if (mongoTemplate != null) {
			mongoTemplate.save(callInfo);
		}
	}

	public List<ProducerUsageData> findByProducerName(String producerName) {
		if (mongoTemplate != null) {
			return mongoTemplate.find(query(where("producerName").is(producerName)), ProducerUsageData.class);
		}
		return Collections.emptyList();
	}

	public List<ProducerUsageData> findAll() {
		if (mongoTemplate != null) {
			return mongoTemplate.findAll(ProducerUsageData.class);
		}
		return Collections.emptyList();
	}

	public void deleteAll() {
		if (mongoTemplate != null) {
			mongoTemplate.remove(new Query(), ProducerUsageData.class);
		}
	}

	@Override
	public Iterator<ProducerUsageData> getValues() {
		// TODO Auto-generated method stub
		return null;
	}
}