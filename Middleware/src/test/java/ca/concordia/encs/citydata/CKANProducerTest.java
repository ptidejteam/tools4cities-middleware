package ca.concordia.encs.citydata;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.concordia.encs.citydata.producers.CKANProducer;

/* CKANProducer tests
 * Author: Gabriel C. Ullmann
 * Date: 2025-02-12
 */
public class CKANProducerTest {

	private CKANProducer ckanProducer;

	@BeforeEach
	void setUp() {
		ckanProducer = new CKANProducer();
	}

	// TODO: write cases
	@Test
	void case1() {
		List<String> result = ckanProducer.getResult();
		assertThat(result).isNull();
	}

}
