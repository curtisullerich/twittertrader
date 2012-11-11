package classification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Label;

public class Classify {
	public static void main(String... args) throws ClassNotFoundException,
			IOException {
		classify();
	}

	public static void classify() throws IOException, ClassNotFoundException {
		Scanner s = new Scanner(new File("../localcorpora/toclassify4.csv"));
		// ObjectInputStream ois = new ObjectInputStream(new
		// FileInputStream(""));
		// Classifier classifier = (Classifier) ois.readObject();
		// ois.close();
		Classifier companyClassifier = ModelTester.getCompanyClassifier();
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File("../companyClassifer3.mallet")));
		oos.writeObject(companyClassifier);
		oos.close();

		Classifier sentimentClassifier = ModelTester
				.getBestSentimentClassifier();
		ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(
				new File("../sentimentClassifer3.mallet")));
		oos2.writeObject(sentimentClassifier);
		oos2.close();
		System.exit(0);

		FileWriter out = new FileWriter(new File(
				"../corpus/newclassifications4.txt"));

		while (s.hasNextLine()) {
			String nextline = s.nextLine();
			String[] line = nextline.split("\",\"");
			if (line.length >= 5 && line[5].equals("en")) { // english only
				String tweet = line[line.length - 1];
				tweet = tweet.substring(0, tweet.length() - 1);
				String id = line[0].substring(1);
				Classification clCompany = companyClassifier.classify(tweet);
				Label labelCompany = clCompany.getLabeling().getBestLabel();
				double valueCompany = clCompany.getLabeling().getBestValue();
				Classification clSentiment = sentimentClassifier
						.classify(tweet);
				Label labelSentiment = clSentiment.getLabeling().getBestLabel();
				double valueSentiment = clSentiment.getLabeling()
						.getBestValue();
				out.append(id + "," + labelCompany + "," + valueCompany + ","
						+ labelSentiment + "," + valueSentiment + "\n");
				// out.append(nextline + ",\"company: " + label + "\"\n");
				System.out
						.println(labelCompany + " (" + valueCompany + ") "
								+ labelSentiment + " (" + valueSentiment + ") "
								+ tweet);
			}
		}
		out.flush();
		out.close();
	}

}
