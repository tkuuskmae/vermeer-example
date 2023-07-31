package dev.brachtendorf.jimagehash_examples;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;

/**
 * An example demonstrating how two images can be compared at a time using a single algorithm
 * 
 * @author Kilian
 *
 */
public class CompareImages {

	// Key bit resolution
	private int keyLength = 262144;

	// Pick an algorithm
	private HashingAlgorithm hasher = new DifferenceHash(keyLength, DifferenceHash.Precision.Triple);

	// Images used for testing
	private HashMap<String, BufferedImage> images = new HashMap<>();

	
	public CompareImages() throws IOException {

		loadImages();

		// Compare each picture to each other
		String comparableName = "original";
		BufferedImage comparableFile = ImageIO.read(new FileInputStream("images/original.png"));

		images.forEach((imageName, image) -> {
			formatOutput(comparableName, imageName, compareTwoImages(image, comparableFile));
		});
	}

	/**
	 * Compares the similarity of two images.
	 * @param image1	First image to be matched against 2nd image
	 * @param image2	The second image
	 * @return	true if the algorithm defines the images to be similar.
	 */
	public double compareTwoImages(BufferedImage image1, BufferedImage image2) {

		//Generate the hash for each image
		Hash hash1 = hasher.hash(image1);
		Hash hash2 = hasher.hash(image2);

		//Compute a similarity score
		// Ranges between 0 - 1. The lower the more similar the images are.
		return hash1.normalizedHammingDistance(hash2);
	}

	//Utility function
	private void formatOutput(String image1, String image2, double similar) {
		String format =  "%-11s | %-11s | %-11s %n";
		System.out.printf(format, image1, image2, similar);
	}
	
	private void loadImages() {
		// Load images
		try {
			for(int i = 1; i <= 10; i++){
				String pointer = String.valueOf(i);
				images.put("sample_" + pointer, ImageIO.read(new FileInputStream("images/sample_" + pointer + ".jpg")));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Print header
		System.out.println("|   Image 1   |   Image 2   | Result  |");
	}

	public static void main(String[] args) throws IOException {
		new CompareImages();
	}

}
