package classification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import model.TweetInstance;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class TweetGrabber {

	private static final String defaultTweetUrl = "http://danielstiner.com:9001/unclassified/random/5";
	private static final String defaultPostUrl = "";

	public void getAndWriteTweets() throws URISyntaxException, HttpException, IOException {
		getAndWriteTweetsFrom(defaultTweetUrl);
	}

	public void getAndWriteTweetsFrom(String url) throws URISyntaxException, HttpException, IOException {
		StringBuilder json = getTweetsFrom(url);

		List<TweetInstance> tweetList = parseTweetsFromJson(json.toString());
		
		//This will need to return something so we can pass it to the
		//next method
		classifyTweets(tweetList);

		writeTweetsToServer(tweetList);
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
	
	private void classifyTweets(List<TweetInstance> tweetList) {
		// TODO Auto-generated method stub
		
	}

	private void writeTweetsToServer(List<TweetInstance> tweetList) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {
		try {
			new TweetGrabber().getAndWriteTweets();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
	}
}
