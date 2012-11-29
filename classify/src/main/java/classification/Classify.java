package classification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import model.TweetJsonIterator;
import model.TweetJsonIterator.Mode;
import model.TweetJsonIterator.Type;
import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Label;

import common.Constants;
import common.FileUtil;
import common.PipeFactory;

public class Classify {
	public static void main(String... args) throws ClassNotFoundException,
			IOException {
		// classify();
		stdio();
	}

	/**
	 * Takes json tweets, one instance per line, from std.in and writes to
	 * std.out in the format
	 * "tweetid, companyclassification, companyconfidence, sentimentclassification, sentimentconfidence"
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void stdio() throws ClassNotFoundException, IOException {
		Scanner s = new Scanner(System.in);

		// Classifier companyClassifier = (new
		// ModelUpdater()).createNewCompanyWikipediaModel();
		Classifier companyClassifier = FileUtil
				.loadBestClassifier(Constants.COMPANY_MODEL);
		Classifier sentimentClassifier = FileUtil
				.loadBestClassifier(Constants.SENTIMENT_MODEL);

		while (s.hasNextLine()) {
			InstanceList il = new InstanceList(PipeFactory.getDefault());
			String line = s.nextLine();
			TweetJsonIterator tji = new TweetJsonIterator(line, Mode.CLASSIFY,
					Type.UNCLASSIFIED);
			il.addThruPipe(tji);
			for (Instance i : il) {// should just be one
				Classification cc = companyClassifier.classify(i);
				Classification sc = sentimentClassifier.classify(i);

				System.out.println(i.getName() + ", "
						+ cc.getLabeling().getBestLabel() + ", "
						+ cc.getLabeling().getBestValue() + ", "
						+ sc.getLabeling().getBestLabel() + ", "
						+ sc.getLabeling().getBestValue());
			}
			// il.clear();
		}
	}

	public static void classify() throws IOException, ClassNotFoundException {
		Scanner s = new Scanner(new File(
				"../localcorpora/classificationset/new0.json"));
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
				"../localcorpora/classificationset/classified0.txt"));

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
