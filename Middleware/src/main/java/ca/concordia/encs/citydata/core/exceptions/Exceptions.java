package ca.concordia.encs.citydata.core.exceptions;

/* This java class contains definitions of custom exceptions
 * Author: Sikandar Ejaz
 * Date: 2-8-2025
 */

public class Exceptions extends RuntimeException {

	public Exceptions(String message) {
		super(message);
	}

	public static class InvalidProducerException extends Exceptions {
		public InvalidProducerException(String message) {
			super(message);
		}
	}

	public static class InvalidOperationException extends Exceptions {
		public InvalidOperationException(String message) {
			super(message);
		}
	}

	public static class InvalidProducerParameterException extends Exceptions {
		public InvalidProducerParameterException(String message) {
			super(message);
		}
	}

	public static class InvalidOperationParameterException extends Exceptions {
		public InvalidOperationParameterException(String message) {
			super(message);
		}
	}

	public static class UnsupportedParameterTypeException extends Exceptions {
		public UnsupportedParameterTypeException(String message) {
			super(message);
		}
	}
}
