package ca.concordia.ngci.toolsforcitiesmiddleware.consumer;

import java.util.List;

public interface IConsumer<E> {
	void receiveData(List<E> data);
}
