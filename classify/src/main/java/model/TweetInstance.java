package model;

import cc.mallet.types.Instance;

public class TweetInstance extends Instance {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 998705805022838098L;

	/**
	 * We only pass in the JSON source text so it can be parsed later by the pipe
	 * @param source
	 */
	public TweetInstance(String source) {
		//Data, target, name, source
		super("JSON", "", "Tweet", source);
	}
}
