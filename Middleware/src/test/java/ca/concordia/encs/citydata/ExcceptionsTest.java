package ca.concordia.encs.citydata;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.concordia.encs.citydata.core.AbstractOperation;
import ca.concordia.encs.citydata.core.exceptions.Exceptions;

/* This java class contains tests for custom exceptions
 * Author: Sikandar Ejaz
 * Date: 2-8-2025
 */

public class ExcceptionsTest {
	private AbstractOperation<String> abstractOperation;

	@BeforeEach
	void OperationsetUp() {
		abstractOperation = new AbstractOperation<>() {
			@Override
			public ArrayList<String> apply(ArrayList<String> input) {
				return super.apply(input);
			}
		};
	}

	@Test
	void testNullInputThrowsInvalidOperationParameterException() {
		assertThatExceptionOfType(Exceptions.InvalidOperationParameterException.class)
				.isThrownBy(() -> abstractOperation.apply(null))
				.withMessageContaining("Input data is null or empty. Cannot perform the operation.");
	}
}
