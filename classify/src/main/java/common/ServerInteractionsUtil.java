package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;

public class ServerInteractionsUtil {

	/**
	 * A Method to write all the tweets given to the server
	 * 
	 * @param jsa
	 *            The JsonArray holding all of the tweets
	 * @throws HttpException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void writeTweetsToServer(JsonArray jsa) throws HttpException,
			IOException, URISyntaxException {
		HttpPost request = new HttpPost(Constants.CLASSIFIED_TWEET_POST_URL);
		request.addHeader("content-type", "application/x-json");
		String tost = jsa.toString();
		// System.out.println(tost);
		StringEntity entity = new StringEntity(tost);
		request.setEntity(entity);
		HttpClient client = new DefaultHttpClient();
		// HttpResponse response = client.execute(request);
		client.getConnectionManager().shutdown();
	}

	/**
	 * A method to get tweets from the server
	 * @param url
	 * 				The url to find the tweets at
	 * @return
	 * 			The Json version of the tweets in a StringBuilder
	 * @throws URISyntaxException
	 * @throws HttpException
	 * @throws IOException
	 */
	public static StringBuilder getTweetsFrom(String url) throws URISyntaxException,
			HttpException, IOException {
		HttpGet getUrl = new HttpGet(url);

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(getUrl);

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), Constants.CHAR_SET));
		StringBuilder builder = new StringBuilder();

		String in = reader.readLine();
		while (in != null) {
			builder.append(in).append("\n");
			in = reader.readLine();
		}
		return builder;
	}

}
