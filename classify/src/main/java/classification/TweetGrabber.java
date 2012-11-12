package classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import cc.mallet.types.Label;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class TweetGrabber {

	private static final String defaultTweetUrl = "http://danielstiner.com:9001/unclassified/random/1";
	private static final String defaultPostUrl = "";
	
	private static final String localTweets = "C:/Users/Brandon/Documents/School/ComS572/Project/TweetJSON.txt";

	public void getAndWriteTweets() throws URISyntaxException, HttpException, IOException {
		getAndWriteTweetsFrom(defaultTweetUrl);
	}

	public void getAndWriteTweetsFrom(String url) throws URISyntaxException, HttpException, IOException {
		//StringBuilder json = getTweetsFrom(url);
		StringBuilder json = getTweetsFromLocal(localTweets);

		List<TweetInstance> tweetList = parseTweetsFromJson(json.toString());
		
		//Classifies the tweets and adds the Labels and values to the tweetInstance
		classifyTweets(tweetList);

		writeTweetsToServer(tweetList);
	}
	
	public StringBuilder getTweetsFromLocal(String file) throws FileNotFoundException {
		StringBuilder builder = new StringBuilder();
		
		Scanner in = new Scanner(new File(file));
		
		while(in.hasNextLine()) {
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

	private List<TweetInstance> parseTweetsFromJson(String string) {
		JsonParser parser = new JsonParser();
		
		//Returns an array of all the json objects
		JsonArray ele = parser.parse(string).getAsJsonArray();

		List<TweetInstance> tweets = new ArrayList<TweetInstance>();
		
		for (int i = 0; i < ele.size(); ++i) {
			tweets.add(new TweetInstance(ele.get(i).toString()));
		}
		return tweets;
	}
	
	private void classifyTweets(List<TweetInstance> tweetList) throws IOException {
		Classifier companyClassifier = null;
		Classifier sentimentClassifier = null;
		if(!companyClassifierExistsOnDisk()) {
			companyClassifier = ModelTester.getCompanyClassifier();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
					new File("../TweetGrabberCompanyModel.mallet")));
			oos.writeObject(companyClassifier);
			oos.close();
		}
		else {
			//classifier = readClassifierFromDisk();
		}
		if(!sentimentClassifierExistsOnDisk()) {
			sentimentClassifier = ModelTester.getBestSentimentClassifier();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
					new File("../TweetGrabberSentimentModel.mallet")));
			oos.writeObject(sentimentClassifier);
			oos.close();
		}
		else {
			
		}
		Classification[] companyClassifications = companyClassifier.classify(tweetList.toArray(new TweetInstance[]{}));
		Classification[] sentimentClassifications = sentimentClassifier.classify(tweetList.toArray(new TweetInstance[]{}));
		
		for (int i = 0; i < tweetList.size(); ++i) {
			Label companyLabel = companyClassifications[i].getLabeling().getBestLabel();
			double companyValue = companyClassifications[i].getLabeling().getBestValue();
			
			Label sentimentLabel = sentimentClassifications[i].getLabeling().getBestLabel();
			double sentimentValue = sentimentClassifications[i].getLabeling().getBestValue();
			
			TweetInstance cur = tweetList.get(i);
			//TODO possibly make the TweetInstance's source a JSON object so we can call add on it
//			cur.add("companyLabel", companyLabel);
//			cur.add("companyValue", companyValue);
//			cur.add("sentimentLabel", sentimentLabel);
//			cur.add("sentimentValue", sentimentValue);
		}
	}

	private boolean companyClassifierExistsOnDisk() {
		//TODO checks to see if there is a classifier saved on disk
		return false;
	}
	
	private boolean sentimentClassifierExistsOnDisk() {
		//TODO
		return false;
	}

	private void writeTweetsToServer(List<TweetInstance> tweetList) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) throws URISyntaxException, HttpException, IOException {
			new TweetGrabber().getAndWriteTweets();
	}
}
