package pv3199.lib.java.image;

import java.awt.image.BufferedImage;
import java.util.function.IntFunction;

public final class ImageUtils {
	/**
	 * Grayscale method enumerations.
	 */
	public enum GrayscaleMethod {
		/**
		 * Averages the largest and smallest RGB values.
		 */
		LIGHTNESS(rgb -> {
			int r = rgb >> 16 & 0xff;
			int g = rgb >> 8 & 0xff;
			int b = rgb & 0xff;

			r = g = b = (Math.max(Math.max(r, g), b) + Math.min(Math.min(r, g), b)) / 2;

			return r << 16 + g << 8 + b;
		}),

		/**
		 * Averages the RGB values.
		 */
		AVERAGE(rgb -> {
			int r = rgb >> 16 & 0xff;
			int g = rgb >> 8 & 0xff;
			int b = rgb & 0xff;

			return (r / 3) << 16 + (g / 3) << 8 + b / 3;
		}),

		/**
		 * Weights the individual RGB values, multiplying red by 0.2126, green by .7152,
		 * and blue by .0722
		 */
		LUMINOSITY(rgb -> {
			int r = rgb >> 16 & 0xff;
			int g = rgb >> 8 & 0xff;
			int b = rgb & 0xff;

			return (int) (r * 0.2126) << 16 + (int) (g * 0.7152) + (int) (b * 0.0722);
		});

		/**
		 * The grayscaling method implementation.
		 */
		private IntFunction<Integer> method;

		/**
		 * Constructs a GrayscaleMethod enumeration given a IntFunction that represents
		 * the mathematical grayscaling method.
		 * @param method the grayscaling method.
		 */
		GrayscaleMethod(IntFunction<Integer> method) {
			this.method = method;
		}

		/**
		 * Grayscales a 24-bit RGB value.
		 * @param rgb the rgb value to grayscale.
		 * @return the RGB value grayscaled.
		 */
		public int grayscale(int rgb) {
			return this.method.apply(rgb);
		}
	}

	/**
	 * Isolates the red values in an image.
	 * @param image the image.
	 * @return the image with the red values isolated.
	 */
	public static BufferedImage isolateRed(BufferedImage image) {
		return isolateColor(image, 0);
	}

	/**
	 * Isolates the green values in an image.
	 * @param image the image.
	 * @return the image with the green values isolated.
	 */
	public static BufferedImage isolateGreen(BufferedImage image) {
		return isolateColor(image, 1);
	}

	/**
	 * Isolates the blue values in an image.
	 * @param image the image.
	 * @return the image with the blue values isolated.
	 */
	public static BufferedImage isolateBlue(BufferedImage image) {
		return isolateColor(image, 2);
	}

	/**
	 * Isolates a color in an image.
	 * @param image the image.
	 * @param color the color to isolate: (0 - red, 1 - green, anything else - blue)
	 * @return the image with the particular color isolated.
	 */
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

	/**
	 * Converts a 24-bit RGB color to black and white using the luminosity grayscaling method.
	 * @param rgb the rgb color.
	 * @return the black and white conversion of the rgb color.
	 */
	public static int rgbToGrayscale(int rgb) {
		return rgbToGrayscale(rgb, GrayscaleMethod.LUMINOSITY);
	}

	/**
	 * Converts a 24-bit RGB color to grayscale given a grayscaling conversion method.
	 * @param rgb the rgb color.
	 * @param method the grayscaling method.
	 * @return the grayscale conversion of the rgb color.
	 * @throws NullPointerException if the conversion method is null
	 */
	public static int rgbToGrayscale(int rgb, GrayscaleMethod method) throws NullPointerException {
		if (method == null) {
			throw new NullPointerException();
		}

		return method.grayscale(rgb);
	}

	/**
	 * Grayscales an image using the luminosity grayscaling method.
	 * @param image the image to grayscale.
	 * @return the grayscaled image.
	 */
	public static BufferedImage imgToGrayscale(BufferedImage image) {
		return imgToGrayscale(image, GrayscaleMethod.LUMINOSITY);
	}

	/**
	 * Grayscales an image using a given grayscaling method.
	 * @param image the image to grayscale.
	 * @param method the grayscaling method
	 * @return the grayscaled image.
	 */
	public static BufferedImage imgToGrayscale(BufferedImage image, GrayscaleMethod method) throws NullPointerException {
		if (method == null) {
			throw new NullPointerException();
		}

		BufferedImage grayscaled = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				int gray = rgbToGrayscale(rgb, method);
				grayscaled.setRGB(x, y, gray);
			}
		}

		return grayscaled;
	}
}
