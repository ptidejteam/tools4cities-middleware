package ca.concordia.encs.citydata.datastores;

import ca.concordia.encs.citydata.core.IDataStore;
import ca.concordia.encs.citydata.core.MiddlewareEntity;
import ca.concordia.encs.citydata.core.ProducerCallInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
/***
 * This class is responsible for storing and retrieving data from MongoDB. Data remains persists even after the application
 * stops.
 * @Author: Rushin Makwana
 * @Date: 26/2/2024
 */
@Component
public class MongoDataStore extends MiddlewareEntity implements IDataStore<ProducerCallInfo> {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void set(String key, ProducerCallInfo value) {
        mongoTemplate.save(value, key);
    }

    @Override
    public ProducerCallInfo get(String key) {
        return mongoTemplate.findById(key, ProducerCallInfo.class);
    }

    @Override
    public void delete(String key) {
        mongoTemplate.remove(query(where("_id").is(key)), ProducerCallInfo.class);
    }

    public void save(ProducerCallInfo callInfo) {
        mongoTemplate.save(callInfo);
    }

    public List<ProducerCallInfo> findByProducerName(String producerName) {
        return mongoTemplate.find(
                query(where("producerName").is(producerName)),
                ProducerCallInfo.class
        );
    }

    public List<ProducerCallInfo> findAll() {
        return mongoTemplate.findAll(ProducerCallInfo.class);
    }
    public void deleteAll() {
        mongoTemplate.remove(new Query(), ProducerCallInfo.class);
    }
}