package classification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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
import common.Constants;
import common.FileUtil;

public class ModelUpdater {

	public static void main(String... args) throws ClassNotFoundException,
			URISyntaxException, HttpException, IOException {
		new ModelUpdater().createModels();
	}

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
		StringBuilder verifiedCountJson = getJsonFrom(Constants.VERIFIED_COUNTS_URL);
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
				String tweeturl = Constants.VERIFIED_URL_PRE + e.getKey() + Constants.VERIFIED_URL_POST
						+ min;
				System.out.println("Getting tweets from " + tweeturl);
				String cur = getJsonFrom(tweeturl).toString();
				TweetJsonIterator tji = new TweetJsonIterator(cur, Mode.TRAIN,
						Type.COMPANY);
				il.addThruPipe(tji);
			}
		}
		Classifier c = trainer.train(il);
		FileUtil.saveClassifiertoDisk(c, Constants.COMPANY_MODEL);
	}



	private StringBuilder getJsonFrom(String url) throws URISyntaxException,
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
