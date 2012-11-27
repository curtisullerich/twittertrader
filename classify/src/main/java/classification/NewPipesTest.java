package classification;

import java.io.IOException;
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

public class NewPipesTest {
	
	public static void main(String[] args) throws HttpException, IOException, URISyntaxException, ClassNotFoundException {
		StringBuilder json = FileUtil.getTweetsFromLocal("C://Users//Brandon//Documents//School//ComS572//Project//TweetJSON.txt");

		TweetJsonIterator tji = new TweetJsonIterator(json.toString(),
				Mode.CLASSIFY, Type.UNCLASSIFIED);

		// List<TweetInstance> tweetList = ModelUpdater.parseTweetsFromJson(
		// json.toString(), true);

		// Classifies the tweets and adds the Labels and values to the
		// tweetInstance
		InstanceList il = new InstanceList(PipeFactory.brandonsGetPipes());
		Classifier companyClassifier = FileUtil
				.loadBestClassifier(Constants.COMPANY_MODEL);
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

	private static String getcstamp(boolean pretty) {
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
}
