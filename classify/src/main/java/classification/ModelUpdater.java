package classification;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import model.TweetJsonIterator;
import model.TweetJsonIterator.Mode;
import model.TweetJsonIterator.Type;

import org.apache.http.HttpException;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.types.InstanceList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.Constants;
import common.FileUtil;
import common.PipeFactory;
import common.ServerInteractionsUtil;

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
		StringBuilder verifiedCountJson = ServerInteractionsUtil
				.getTweetsFrom(Constants.VERIFIED_COUNTS_URL);
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
		InstanceList il = new InstanceList(PipeFactory.getPipe4());
		int i = 0;
		for (Entry<String, JsonElement> e : counts.entrySet()) {
			if (e.getValue().getAsInt() > 0) {
				String tweeturl = Constants.VERIFIED_URL_PRE + e.getKey() + Constants.VERIFIED_URL_POST
						+ min;
				System.out.println("Getting tweets from " + tweeturl);
				String cur = ServerInteractionsUtil.getTweetsFrom(tweeturl).toString();
				TweetJsonIterator tji = new TweetJsonIterator(cur, Mode.TRAIN,
						Type.COMPANY);
				il.addThruPipe(tji);
			}
		}
		Classifier c = trainer.train(il);
		FileUtil.saveClassifiertoDisk(c, Constants.COMPANY_MODEL);
	}
}
