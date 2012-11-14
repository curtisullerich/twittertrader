package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;

import cc.mallet.classify.Classifier;

public class FileUtil {

	/**
	 * A Method to write a Classifier to disk
	 * 
	 * @param c
	 *            the classifier to be written
	 * @param modelType
	 *            The type of model this is, ie company or sentiment
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveClassifiertoDisk(Classifier c, String modelType)
			throws FileNotFoundException, IOException {
		Date date = new Date();
		//Since windows doesn't allow ":" in the file name we 
		//need to check which os it is to know what to save it as
		//Or maybe just do
		//String stamp = Constants.UGLY_SDF;
		String stamp = (isWindowsOS() ? Constants.UGLY_SDF.format(date) : Constants.PRETTY_SDF.format(date));
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File("../" + modelType + stamp + ".mallet")));
		oos.writeObject(c);
		oos.close();
	}
	
	private static boolean isWindowsOS() {
		String os = System.getProperty("os.name");
		return os != null ? os.toLowerCase().contains("windows") : false;
	}

	/**
	 * A method to load a classifier from disk
	 * 
	 * @param file
	 *            the file where the classifier is located
	 * @return a classifier object from the given file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Classifier loadClassifierFromDisk(File file)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Classifier classifier = (Classifier) ois.readObject();
		ois.close();
		return classifier;
	}

	/**
	 * A method to return the most recent classifier found on disk, or null 
	 * if there is no most recent classifier
	 * @param modelType
	 * 					The type of the model you want to load
	 * @return
	 * 			A classifier object that was most recently saved, or null
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Classifier loadMostRecentClassifierFromDisk(String modelType)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		File mostRecent = getMostRecentFile(modelType);
		return (mostRecent != null) ? loadClassifierFromDisk(mostRecent) : null;
	}

	/**
	 * A method to return the most recently saved file that contains the string
	 * modelType in the file name
	 * 
	 * @param pattern
	 *            the pattern to look for
	 * @return
	 */
	public static File getMostRecentFile(String modelPattern) {
		File largest = null;
		for (File f : (new File("../")).listFiles(new Filter(modelPattern))) {
			if (largest == null) {
				largest = f;
			}
			if (f.getName().compareToIgnoreCase(largest.getName()) > 0) {
				largest = f;
			}
		}
		return largest;
	}

	private static class Filter implements FilenameFilter {
		String pattern;

		public Filter(String pattern) {
			this.pattern = pattern;
		}

		public boolean accept(File dir, String name) {
			return name.contains(pattern);
		}
	}
	
	/**
	 * A method to read tweets that are saved locally
	 * @param filename
	 * 					The filename of the file holding the tweets
	 * @return
	 * 			A StringBuilder holding all of the local tweets
	 * @throws FileNotFoundException
	 */
	public static StringBuilder getTweetsFromLocal(String filename)
			throws FileNotFoundException {
		StringBuilder builder = new StringBuilder();

		Scanner in = new Scanner(new File(filename));

		while (in.hasNextLine()) {
			builder.append(in.nextLine()).append("\n");
		}
		return builder;
	}
}
