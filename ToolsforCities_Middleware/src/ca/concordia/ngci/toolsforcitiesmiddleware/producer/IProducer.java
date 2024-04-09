package ca.concordia.ngci.toolsforcitiesmiddleware.producer;

import java.io.IOException;

public interface IProducer<E> {
	E fetchData() throws IOException;
}
