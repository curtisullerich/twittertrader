package classification;

import model.TweetInstance;
import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

import com.google.gson.JsonElement;
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
//		System.out.println("in the pipe method");
		if (!(carrier instanceof TweetInstance)) {
//			System.out.println("not a tweet instanace!");
			return carrier;
		} else {
//			System.out.println("tweet instance!----------------------------------------------------------------");
			TweetInstance tweetI = (TweetInstance) carrier;
			JsonElement e = (JsonElement) carrier.getSource();
//			String data = getTweetText((String) tweetI.getSource());
//
			tweetI.setData(e.getAsJsonObject().get("text").getAsString());
			return tweetI;
		}
	}

	// @Override
	// public Iterator<Instance> newIteratorFrom(Iterator<Instance> instances) {
	// System.out.println("in the iterator method");
	// List<Instance> classifiedInstances = new ArrayList<Instance>();
	//
	// while (instances.hasNext()) {
	// Instance next = instances.next();
	//
	// classifiedInstances.add(pipe(next));
	// }
	// return classifiedInstances.iterator();
	// }
	
	private String getTweetText(String source) {
		JsonParser parser = new JsonParser();
		JsonObject o = (JsonObject) parser.parse(source);
		// TODO do we want to do anything here if the JsonObject has already
		// been classified?
		String text = o.get("text").getAsString();

//		System.out.println("text: " + text);
		return text;
	}
}
