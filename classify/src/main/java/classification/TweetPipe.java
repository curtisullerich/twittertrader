package classification;

import model.TweetInstance;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TweetPipe extends Pipe {

	public TweetPipe() {
		super();
	}
	
	/**
	 * Takes the instance, grabs the source and sets the Instance's target as just
	 * the tweet text
	 * @param carrier
	 * @return
	 */
	public Instance pipe (Instance carrier) {
		if (!(carrier instanceof TweetInstance)) {
			return carrier;
		}
		else {
			TweetInstance tweetI = (TweetInstance) carrier;
			
			String target = getTweetText((String)tweetI.getSource());
			
			tweetI.setTarget(target);
			
			return tweetI;
		}
	}
	
	private static String getTweetText(String source) {
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject) parser.parse(source);
		
		return o.get("text").getAsString();
	}
}
