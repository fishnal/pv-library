package pv3199.lib.java.image;

import java.awt.image.BufferedImage;

public final class ImageUtils {
	public static BufferedImage isolateRed(BufferedImage image) {
		return isolateColor(image, 0);
	}
	
	public static BufferedImage isolateGreen(BufferedImage image) {
		return isolateColor(image, 1);
	}
	
	public static BufferedImage isolateBlue(BufferedImage image) {
		return isolateColor(image, 2);
	}
	
	private static BufferedImage isolateColor(BufferedImage image, final int color) {
		BufferedImage isolated = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		final int shift = color == 0 ? 16 : color == 1 ? 8 : 0;
		
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				int ic = rgb >> shift & 0xff;
				isolated.setRGB(x, y, ic << shift);
			}
		}
		
		return isolated;
	}
}
