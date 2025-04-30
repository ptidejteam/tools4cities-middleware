package ca.concordia.encs.citydata.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import ca.concordia.encs.citydata.datastores.DiskDatastore;
import ca.concordia.encs.citydata.datastores.InMemoryDataStore;
import ca.concordia.encs.citydata.datastores.MongoDataStore;

/***
 * This is the Spring Boot application entry point.
 * 
 * @author Gabriel C. Ullmann, Sikandar Ejaz, Rushin Makwana
 * @date 2025-01-01
 */

@SpringBootApplication
@ComponentScan(basePackages = { "ca.concordia.encs.citydata.core.controllers",
		"ca.concordia.encs.citydata.core.configs", "ca.concordia.encs.citydata.datastores" })
public class Application {

	// initialize all datastore for later use
	InMemoryDataStore memoryStore = InMemoryDataStore.getInstance();
	DiskDatastore diskStore = DiskDatastore.getInstance();
	MongoDataStore mongoDataStore = MongoDataStore.getInstance();

	public static String convertInt96ToDateTimeString(byte[] int96Bytes) {
		if (int96Bytes.length != 12) {
			throw new IllegalArgumentException("INT96 must be exactly 12 bytes");
		}

		// INT96 is little-endian
		ByteBuffer buffer = ByteBuffer.wrap(int96Bytes).order(ByteOrder.LITTLE_ENDIAN);

		long nanosOfDay = buffer.getLong(); // first 8 bytes: nanos since midnight
		int julianDay = buffer.getInt(); // last 4 bytes: Julian day

		// Convert Julian day to epoch day (days since 1970-01-01)
		long epochDay = julianDay - 2440588L;
		long secondsSinceEpoch = epochDay * 86400 + (nanosOfDay / 1_000_000_000);
		int nanos = (int) (nanosOfDay % 1_000_000_000);

		// Convert to LocalDateTime (UTC)
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(secondsSinceEpoch, nanos, ZoneOffset.UTC);

		// Format as string
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public static void main(String[] args) throws IllegalArgumentException {
		ApplicationContext context = SpringApplication.run(Application.class, args);
		MongoDataStore mongoDataStore = context.getBean(MongoDataStore.class);

		String filePath = "/Users/gabrielullmann/Desktop/OPE_Consommation.parquet";

		// Create a configuration object
		Configuration configuration = new Configuration();

		// Enable the flag for reading INT96 as a byte array
		configuration.setBoolean("parquet.avro.readInt96AsFixed", true);

		// Build the AvroParquetReader with the configuration
		try (ParquetReader<GenericRecord> reader = AvroParquetReader.<GenericRecord>builder(new Path(filePath))
				.withConf(configuration) // Pass the configuration with INT96 flag
				.build()) {

			GenericRecord record;
			int rowIndex = 0;
			while ((record = reader.read()) != null && rowIndex < 100) {

				if ("0.7686".equals(record.get("energieactivelivree_kwh").toString())) {
					System.out.println("Row " + rowIndex + ": " + record);
					System.out.println(convertInt96ToDateTimeString((byte[]) record.get("dateinterval")));
				}

				rowIndex++;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}