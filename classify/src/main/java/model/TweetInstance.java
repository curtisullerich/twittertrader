package model;

import cc.mallet.types.Instance;

import com.google.gson.JsonElement;

public class TweetInstance extends Instance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 998705805022838098L;

	/**
	 * We only pass in the JSON source text so it can be parsed later by the
	 * pipe
	 * 
	 * @param source
	 */
	public TweetInstance(JsonElement source) {
		// Data, target, name, source
		super("", "", "Tweet", source);
	}

	@Override
	public String toString() {
		// Just returns the Json of this instance
		return ((JsonElement) source).getAsString();
	}
}
