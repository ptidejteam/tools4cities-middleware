package ca.concordia.ngci.tools4cities.middleware.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IConsumer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;
import ca.concordia.ngci.tools4cities.middleware.producers.OccupancyProducer;
import ca.concordia.ngci.tools4cities.middleware.producers.PersonFromJSONProducer;
import ca.concordia.ngci.tools4cities.middleware.producers.PersonFromJSONProducer.Person;

public class Test2 {

	private static final class Test2Consumer<E> extends AbstractConsumer
			implements IConsumer {

		public Test2Consumer(Set<IProducer<?>> setOfProducers) {
			super(setOfProducers);
		}

		@Override
		public void newDataAvailable(final IProducer aProducer)
				throws Exception {

		}

		@Override
		public void newDataAvailable(final IOperation anOperation)
				throws Exception {

			// TODO Implement more detailed assertions
			Assert.assertNotNull(anOperation.fetchData());
		}

		@Override
		public E[] getResults() {
			return null;
		}

	}

	private static final class Test2Operation extends AbstractOperation
			implements IOperation {

		private final List<String> results = new ArrayList<>();
		private final Set<IProducer<?>> setOfProducers;

		public Test2Operation(final Set<IProducer<?>> setOfProducers) {
			super(setOfProducers);
			this.setOfProducers = setOfProducers;
		}

		@Override
		public IProducer<?> fetchData() throws Exception {
			return new Test2OperationProducer(results);
		}

		@Override
		public void newDataAvailable(final IProducer<?> aProducer)
				throws Exception {

			// TODO This is very bad because depends on the order of the producers on which we do not have any control
			final Iterator<IProducer<?>> iterator = this.setOfProducers
					.iterator();
			final IProducer<?> producer2 = iterator.next();
			final IProducer<?> producer1 = iterator.next();

			final List<PersonFromJSONProducer.Person> o1 = (List<PersonFromJSONProducer.Person>) producer1
					.fetchData();
			final Object[] o2 = (Object[]) producer2.fetchData();

			for (Iterator iterator2 = o1.iterator(); iterator2.hasNext();) {
				final Person person = (Person) iterator2.next();
				if (person.getAge() == (int) o2[1]) {
					results.add(person.getName());
				}
			}

			this.notifyObservers();
		}
	}

	private static final class Test2OperationProducer extends
			AbstractProducer<List<String>> implements IProducer<List<String>> {

		private final List<String> results;

		private Test2OperationProducer(List<String> results) {
			this.results = results;
		}

		@Override
		public List<String> fetchData() throws Exception {
			return results;
		}
	}

	@Test
	public void test1() throws Exception {
		final IProducer<List<Person>> producer1 = new PersonFromJSONProducer();
		final IProducer<Object[]> producer2 = new OccupancyProducer();
		final Set<IProducer<?>> setOfProducers = new HashSet<>();
		setOfProducers.add(producer1);
		setOfProducers.add(producer2);

		final IOperation occupancyByAge = new Test2Operation(setOfProducers);

		final IConsumer<?> testerConsumer = new Test2Consumer<?>(
				setOfProducers);

		producer1.start();
		producer2.start();
		occupancyByAge.start();
		testerConsumer.start();
		this.wait(6000);
		// TODO Kill all threads 

		// TODO Implement more detailed assertions
		Assert.assertNotNull(testerConsumer.getResults());
	}

}
