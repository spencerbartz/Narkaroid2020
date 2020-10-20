package com.spencerbartz.narkaroid;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

//Singleton design pattern, woo
public class ImageLoader {
	public final static ImageLoader INSTANCE = new ImageLoader();

	private ImageLoader() {
		// Exists only to defeat instantiation.
	}

	BufferedImage loadImage(String path) {
		BufferedImage newImg = null;

		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream input = classLoader.getResourceAsStream(path);
			newImg = ImageIO.read(input);
		} catch (IOException e) {
			System.err.println("ImageLoader: Image file path " + path);
			e.printStackTrace();
			System.exit(1);
		}

		return newImg;
	}
}
