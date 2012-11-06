package classification;

import model.TweetInstance;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TweetPipe extends Pipe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9003870124716922925L;

	public TweetPipe() {
		super();
	}

	/**
	 * Takes the instance, grabs the source and sets the Instance's target as
	 * just the tweet text. If the Instance is not a TweetInstance, this serves
	 * as NOP.
	 * 
	 * @param carrier
	 * @return
	 */
	public Instance pipe(Instance carrier) {
		if (!(carrier instanceof TweetInstance)) {
			return carrier;
		} else {
			TweetInstance tweetI = (TweetInstance) carrier;

			String target = getTweetText((String) tweetI.getSource());

			tweetI.setTarget(target);

			return tweetI;
		}
	}

	private String getTweetText(String source) {
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject) parser.parse(source);
		// TODO do we want to do anything here if the JsonObject has already
		// been classified?
		return o.get("text").getAsString();
	}
}
