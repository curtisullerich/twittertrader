package classification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.util.Date;

import model.TweetJsonIterator;
import model.TweetJsonIterator.Mode;
import model.TweetJsonIterator.Type;

import org.apache.http.HttpException;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.Constants;
import common.FileUtil;
import common.PipeFactory;
import common.ServerInteractionsUtil;

public class TweetGrabber {
	public void getAndWriteTweets() throws URISyntaxException, HttpException,
			IOException, ClassNotFoundException {
		getAndWriteTweetsFrom(Constants.NEW_TWEET_GET_URL);
	}

	public void getAndWriteTweetsFrom(String url) throws URISyntaxException,
			HttpException, IOException, ClassNotFoundException {
		// StringBuilder json = getTweetsFrom(url);
		// StringBuilder json = getTweetsFromLocal(localTweets);
		StringBuilder json = ServerInteractionsUtil.getTweetsFrom(url);

		TweetJsonIterator tji = new TweetJsonIterator(json.toString(),
				Mode.CLASSIFY, Type.UNCLASSIFIED);

		// List<TweetInstance> tweetList = ModelUpdater.parseTweetsFromJson(
		// json.toString(), true);

		// Classifies the tweets and adds the Labels and values to the
		// tweetInstance
		InstanceList il = new InstanceList(PipeFactory.getDefault());
		Classifier companyClassifier = (new ModelUpdater())
				.createNewCompanyWikipediaModel();
		// Classifier companyClassifier = FileUtil
		// .loadBestClassifier(Constants.COMPANY_MODEL);
		Classifier sentimentClassifier = FileUtil
				.loadBestClassifier(Constants.SENTIMENT_MODEL);
		il.addThruPipe(tji);
		JsonArray jsa = new JsonArray();
		for (Instance i : il) {
			Classification cc = companyClassifier.classify(i);
			Classification sc = sentimentClassifier.classify(i);
			JsonObject jse = new JsonObject();
			jse.addProperty(Constants.ID_PROPERTY_KEY, (String) i.getName());
			jse.addProperty(Constants.COMPANY_PROPERTY_KEY, cc.getLabeling()
					.getBestLabel().toString());
			jse.addProperty(Constants.SENTIMENT_PROPERTY_KEY, sc.getLabeling()
					.getBestLabel().toString());
			jse.addProperty(Constants.COMPANY_CONFIDENCE_PROPERTY_KEY, cc
					.getLabeling().getBestValue());
			jse.addProperty(Constants.SENTIMENT_CONFIDENCE_PROPERTY_KEY, sc
					.getLabeling().getBestValue());
			jse.addProperty(Constants.CSTAMP_PROPERTY_KEY, getcstamp(false));
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

		ServerInteractionsUtil.writeTweetsToServer(jsa);
	}

	// private Classifier getBest(String pattern) throws FileNotFoundException,
	// ClassNotFoundException, IOException {
	// //This can also be replaced by FileUtil.loadMostRecentClassifierFromDisk
	// File largest = FileUtil.getMostRecentFile(pattern);
	// //This can also be replaced by FileUtil.loadMostRecentClassifierFromDisk
	// if (false){//largest != null) {
	// System.out.println("Using classifier: " + largest.getName());
	// return FileUtil.loadClassifierFromDisk(largest);
	// } else {
	// // there was no such file, so create one
	//
	// Classifier c = null;
	// String modelType = null;
	// if (pattern.equals(Constants.COMPANY_MODEL)) {
	// c = ModelFactory.getCompanyClassifier();
	// modelType = Constants.COMPANY_MODEL;
	//
	// }
	// if (pattern.equals(Constants.SENTIMENT_MODEL)) {
	// c = ModelFactory.getBestSentimentClassifier();
	// modelType = Constants.SENTIMENT_MODEL;
	// }
	//
	// FileUtil.saveClassifiertoDisk(c, modelType);
	// return c;
	// }
	// }

	private String getcstamp(boolean pretty) {
		// TODO does this need to use Constants.FRIENDLY_SDF?

		if (pretty) {
			Date date = new Date();
			String stamp = Constants.PRETTY_SDF.format(date);
			return stamp;
		} else {
			Date date = new Date();
			String stamp = Constants.UGLY_SDF.format(date);
			return stamp;
		}
	}

	// private void writeTweetsToServer(JsonArray jsa) throws HttpException,
	// IOException, URISyntaxException {
	// // TODO Auto-generated method stub
	// HttpPost request = new HttpPost(defaultPostUrl);
	// request.addHeader("content-type", "application/x-json");
	// String tost = jsa.toString();
	// // System.out.println(tost);
	// StringEntity entity = new StringEntity(tost);
	// request.setEntity(entity);
	// HttpClient client = new DefaultHttpClient();
	// HttpResponse response = client.execute(request);
	// client.getConnectionManager().shutdown();
	// }

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
