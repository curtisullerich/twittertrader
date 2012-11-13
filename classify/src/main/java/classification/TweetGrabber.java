package classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.TweetInstance;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TweetGrabber {

	private static final String defaultTweetUrl = "http://danielstiner.com:9001/unclassified/random/100";
	private static final String defaultPostUrl = "";

	private static final String localTweets = "C:/Users/Brandon/Documents/School/ComS572/Project/TweetJSON.txt";

	public void getAndWriteTweets() throws URISyntaxException, HttpException,
			IOException, ClassNotFoundException {
		getAndWriteTweetsFrom(defaultTweetUrl);
	}

	public void getAndWriteTweetsFrom(String url) throws URISyntaxException,
			HttpException, IOException, ClassNotFoundException {
		// StringBuilder json = getTweetsFrom(url);
		// StringBuilder json = getTweetsFromLocal(localTweets);
		StringBuilder json = getTweetsFrom(defaultTweetUrl);

		List<TweetInstance> tweetList = ModelUpdater.parseTweetsFromJson(
				json.toString(), true);

		// Classifies the tweets and adds the Labels and values to the
		// tweetInstance
		classifyTweets(tweetList);

		writeTweetsToServer(tweetList);
	}

	public StringBuilder getTweetsFromLocal(String file)
			throws FileNotFoundException {
		StringBuilder builder = new StringBuilder();

		Scanner in = new Scanner(new File(file));

		while (in.hasNextLine()) {
			builder.append(in.nextLine()).append("\n");
		}
		return builder;
	}

	private StringBuilder getTweetsFrom(String url) throws URISyntaxException,
			HttpException, IOException {
		HttpGet getUrl = new HttpGet(defaultTweetUrl);

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(getUrl);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		StringBuilder builder = new StringBuilder();

		String in = reader.readLine();
		while (in != null) {
			builder.append(in).append("\n");
			in = reader.readLine();
		}
		return builder;
	}

	private class Filter implements FilenameFilter {
		String pattern;

		public Filter(String pattern) {
			this.pattern = pattern;
		}

		public boolean accept(File dir, String name) {
			return name.contains(pattern);
		}
	}

	private Classifier getBest(String pattern) throws FileNotFoundException,
			ClassNotFoundException, IOException {
		File largest = null;
		for (File f : (new File("../")).listFiles(new Filter(pattern))) {
			if (largest == null) {
				largest = f;
			}
			if (f.getName().compareToIgnoreCase(largest.getName()) > 0) {
				largest = f;
			}
		}
		if (largest != null) {
			System.out.println("Using classifier: " + largest.getName());
			return loadClassifier(largest);
		} else {
			// there was no such file, so create one
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			Date date = new Date();
			String stamp = sdf.format(date);

			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File("../" + pattern + stamp
							+ ".mallet")));

			Classifier c = null;
			if (pattern.equals("companyModel")) {
				c = ModelTester.getCompanyClassifier();

			}
			if (pattern.equals("sentimentModel")) {
				c = ModelTester.getBestSentimentClassifier();
			}

			oos.writeObject(c);
			oos.close();
			return c;
		}
	}

	private void classifyTweets(List<TweetInstance> tweetList)
			throws IOException, ClassNotFoundException {
		Classifier companyClassifier = getBest("companyModel");
		Classifier sentimentClassifier = getBest("sentimentModel");

		for (TweetInstance ti : tweetList) {

			// we have to do this or when it does the classification it will see
			// that there's already a target there and be sad
			// ti.setTarget(null);
			System.out.println("Target: " + ti.getTarget());
			Classification sentimentC = null;
			Classification companyC = null;
			try {
				companyC = companyClassifier
						.classify(companyClassifier.getInstancePipe()
								.instanceFrom(ti));

				sentimentC = sentimentClassifier.classify(sentimentClassifier
						.getInstancePipe().instanceFrom(ti));
			} catch (IllegalArgumentException e) {
				System.out.println("Target: " + ti.getTarget());
				// System.out.println("Source: " + ti.getSource());
				System.out.println("Name: " + ti.getName());
				System.out.println("----------------------------");

			}
			System.out.println("["
					+ companyC.getLabeling().getBestLabel()
					+ "]"
					+ " ("
					+ companyC.getLabeling().getBestValue()
					+ ") "
					+ sentimentC.getLabeling().getBestLabel()
					+ " ("
					+ sentimentC.getLabeling().getBestValue()
					+ ") "
					+ ((JsonElement) ti.getSource()).getAsJsonObject()
							.get("text").getAsString());
			// System.out.println("Data: " + ti.getData());
			System.out.println("Target: " + ti.getTarget());
			// System.out.println("Source: " + ti.getSource());
			System.out.println("Name: " + ti.getName());
			System.out.println("----------------------------");

		}

		// Classification[] companyClassifications = companyClassifier
		// .classify(tweetList.toArray(new TweetInstance[] {}));
		// Classification[] sentimentClassifications = sentimentClassifier
		// .classify(tweetList.toArray(new TweetInstance[] {}));
		//
		// for (int i = 0; i < tweetList.size(); i++) {
		// Label companyLabel = companyClassifications[i].getLabeling()
		// .getBestLabel();
		// double companyValue = companyClassifications[i].getLabeling()
		// .getBestValue();
		//
		// Label sentimentLabel = sentimentClassifications[i].getLabeling()
		// .getBestLabel();
		// double sentimentValue = sentimentClassifications[i].getLabeling()
		// .getBestValue();
		//
		// TweetInstance cur = tweetList.get(i);
		//
		// System.out.println(companyLabel + " (" + companyValue + ") "
		// + sentimentLabel + " (" + sentimentValue + ") ");
		// System.out.println("Data: " + cur.getData());
		// System.out.println("Target: " + cur.getTarget());
		// System.out.println("Source: " + cur.getSource());
		// System.out.println("Name: " + cur.getName());
		// System.out.println("----------------------------");
		// // TODO possibly make the TweetInstance's source a JSON object so we
		// // can call add on it
		// // cur.add("companyLabel", companyLabel);
		// // cur.add("companyValue", companyValue);
		// // cur.add("sentimentLabel", sentimentLabel);
		// // cur.add("sentimentValue", sentimentValue);
		// }
	}

	private void writeTweetsToServer(List<TweetInstance> tweetList) {
		// TODO Auto-generated method stub

	}

	private Classifier loadClassifier(File file) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Classifier classifier = (Classifier) ois.readObject();
		ois.close();
		return classifier;
	}

	public static void main(String[] args) throws URISyntaxException,
			HttpException, IOException, ClassNotFoundException {
		new TweetGrabber().getAndWriteTweets();
	}
}
