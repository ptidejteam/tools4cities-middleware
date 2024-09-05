package ca.concordia.ngci.tools4cities.middleware.operations;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ca.concordia.ngci.tools4cities.middleware.middleware.IOperation;

/**
 * TBW
 */
public class RotationOperation implements IOperation {

	@Override
	public <Image> List<Image> perform(List<Image> inputs) throws Exception {
	
		List<Image> result = new ArrayList<Image>();
		
		for (Image img : inputs) {
			BufferedImage bi = (BufferedImage) img;
			AffineTransform transform = new AffineTransform();
			transform.rotate(Math.toRadians(90), bi.getWidth() / 2, bi.getHeight() / 2);
			AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
			BufferedImage rotatedImage = op.filter(bi, null);
			result.add((Image) rotatedImage);
		}
		
		return result;
	}

}
