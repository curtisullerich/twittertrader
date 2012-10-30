package model;

import cc.mallet.types.Instance;

public class TweetInstance extends Instance {
	
	/**
	 * We only pass in the JSON source text so it can be parsed later by the pipe
	 * @param source
	 */
	public TweetInstance(String source) {
		//Let these default to their values?
		super("Tweet", "JSON", "", source);
	}
}
