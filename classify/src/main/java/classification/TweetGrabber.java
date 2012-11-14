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
import java.util.Date;
import java.util.Scanner;

import model.TweetJsonIterator;
import model.TweetJsonIterator.Mode;
import model.TweetJsonIterator.Type;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TweetGrabber {

	private static final String defaultTweetUrl = "http://danielstiner.com:9001/unclassified/random/1000";
	private static final String defaultPostUrl = "http://danielstiner.com:9001/classified/classify";

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

		TweetJsonIterator tji = new TweetJsonIterator(json.toString(),
				Mode.CLASSIFY, Type.UNCLASSIFIED);

		// List<TweetInstance> tweetList = ModelUpdater.parseTweetsFromJson(
		// json.toString(), true);

		// Classifies the tweets and adds the Labels and values to the
		// tweetInstance
		InstanceList il = new InstanceList(ModelTester.getPipe4());
		Classifier companyClassifier = getBest("companyModel");
		Classifier sentimentClassifier = getBest("sentimentModel");
		il.addThruPipe(tji);
		JsonArray jsa = new JsonArray();
		for (Instance i : il) {
			Classification cc = companyClassifier.classify(i);
			Classification sc = sentimentClassifier.classify(i);
			JsonObject jse = new JsonObject();
			jse.addProperty("id_str", (String) i.getName());
			jse.addProperty("company", cc.getLabeling().getBestLabel()
					.toString());
			jse.addProperty("sentiment", sc.getLabeling().getBestLabel()
					.toString());
			jse.addProperty("companyConfidence", cc.getLabeling()
					.getBestValue());
			jse.addProperty("sentimentConfidence", sc.getLabeling()
					.getBestValue());
			jse.addProperty("cstamp", getcstamp(false));
			jsa.add(jse);
			System.out.println("["
					+ cc.getLabeling().getBestLabel()
					+ "]"
					+ " ("
					+ cc.getLabeling().getBestValue()
					+ ") ["
					+ sc.getLabeling().getBestLabel()
					+ "]"
					+ " ("
					+ sc.getLabeling().getBestValue()
					+ ") "
					+ ((JsonElement) cc.getInstance().getSource())
							.getAsJsonObject().get("text").getAsString());
		}
		writeTweetsToServer(jsa);

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

			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File("../" + pattern
							+ getcstamp(true) + ".mallet")));

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

	private String getcstamp(boolean pretty) {
		if (pretty) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			Date date = new Date();
			String stamp = sdf.format(date);
			return stamp;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String stamp = sdf.format(date);
			return stamp;
		}
	}

	private void writeTweetsToServer(JsonArray jsa) throws HttpException,
			IOException, URISyntaxException {
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(defaultPostUrl);
		request.addHeader("content-type", "application/x-json");
		String tost = jsa.toString();
	//	System.out.println(tost);
		StringEntity entity = new StringEntity(tost);
		request.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		client.getConnectionManager().shutdown();
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
