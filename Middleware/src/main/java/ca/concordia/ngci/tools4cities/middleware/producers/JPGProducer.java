package ca.concordia.ngci.tools4cities.middleware.producers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import ca.concordia.ngci.tools4cities.middleware.middleware.AbstractProducer;
import ca.concordia.ngci.tools4cities.middleware.middleware.IProducer;

public class JPGProducer extends AbstractProducer<Image> implements IProducer<Image> {
	
	public JPGProducer(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void fetchData() throws Exception {
		final List<Image> images = new ArrayList<Image>();
		String imageString = this.fetchFromPath();
		
		// Decode Base64 string to byte array
        byte[] imageBytes = Base64.getDecoder().decode(imageString);
        
        // Convert byte array to InputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        
        // Read the image from the InputStream
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        
        images.add(bufferedImage);
		this.notifyObservers(images);
	}

}
