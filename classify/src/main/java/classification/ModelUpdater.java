package classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;

import model.TweetJsonIterator;
import model.TweetJsonIterator.Mode;
import model.TweetJsonIterator.Type;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.types.InstanceList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ModelUpdater {

	public static void main(String... args) throws ClassNotFoundException,
			URISyntaxException, HttpException, IOException {
		new ModelUpdater().createModels();
	}

	private static final String defaultTweetUrl = "http://danielstiner.com:9001/unclassified/random/100";
	private static final String defaultPostUrl = "";
	private final static String verifiedUrlPre = "http://danielstiner.com:9001/verified/company/";
	private final static String verifiedUrlPost = "/count/";
	private static final String localTweets = "C:/Users/Brandon/Documents/School/ComS572/Project/TweetJSON.txt";
	private static final String verifiedCountsUrl = "http://danielstiner.com:9001/verified/companies";

	public void createModels() throws URISyntaxException, HttpException,
			IOException, ClassNotFoundException {
		createNewCompanyModel();
		createNewSentimentModel();
	}

	private void createNewSentimentModel() {
		// TODO Auto-generated method stub

	}

	public void createNewCompanyModel() throws URISyntaxException,
			HttpException, IOException, ClassNotFoundException {
		StringBuilder verifiedCountJson = getJsonFrom(verifiedCountsUrl);
		JsonParser parser = new JsonParser();

		JsonObject counts = parser.parse(verifiedCountJson.toString())
				.getAsJsonObject();
		int min = -1;
		for (Entry<String, JsonElement> e : counts.entrySet()) {
			int val = e.getValue().getAsInt();
			if (min == -1 && val > 0) {
				min = val;
			}
			if (val < min && val > 0) {
				min = val;
			}
		}
		min = 57;
		System.out.println("Training with min = " + min + " instances");
		// MaxEntTrainer trainer = new MaxEntTrainer();
		NaiveBayesTrainer trainer = new NaiveBayesTrainer();
		InstanceList il = new InstanceList(ModelTester.getPipe4());
		int i = 0;
		for (Entry<String, JsonElement> e : counts.entrySet()) {
			if (e.getValue().getAsInt() > 0) {
				String tweeturl = verifiedUrlPre + e.getKey() + verifiedUrlPost
						+ min;
				System.out.println("Getting tweets from " + tweeturl);
				String cur = getJsonFrom(tweeturl).toString();
				TweetJsonIterator tji = new TweetJsonIterator(cur, Mode.TRAIN,
						Type.COMPANY);
				il.addThruPipe(tji);
			}
		}
		Classifier c = trainer.train(il);
		writeModeltoDisk(c, "companyModel");
	}

	public static void writeModeltoDisk(Classifier c, String prepend)
			throws FileNotFoundException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Date date = new Date();
		String stamp = sdf.format(date);

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File("../" + prepend + stamp + ".mallet")));
		oos.writeObject(c);
		oos.close();
	}

	// public StringBuilder getTweetsFromLocal(String path)
	// throws FileNotFoundException {
	// StringBuilder builder = new StringBuilder();
	// Scanner in = new Scanner(new File(path));
	// while (in.hasNextLine()) {
	// builder.append(in.nextLine()).append("\n");
	// }
	// return builder;
	// }

	private StringBuilder getJsonFrom(String url) throws URISyntaxException,
			HttpException, IOException {
		HttpGet getUrl = new HttpGet(url);

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

	// public static List<TweetInstance> parseTweetsFromJson(String string,
	// boolean setTargets) {
	// JsonParser parser = new JsonParser();
	//
	// // Returns an array of all the json objects
	// JsonArray ele = parser.parse(string).getAsJsonArray();
	//
	// List<TweetInstance> tweets = new ArrayList<TweetInstance>();
	//
	// for (JsonElement e : ele) {
	//
	// JsonElement dan = e.getAsJsonObject().get("text");
	// if (dan != null) {
	// TweetInstance ins = new TweetInstance(e);
	// if (setTargets) {
	// ins.setCompanyTarget();
	// }
	// tweets.add(ins);
	//
	// }
	// }
	// return tweets;
	// }
}
