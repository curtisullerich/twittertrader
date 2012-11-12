package model;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
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
		super(new FeatureVector(new Alphabet(), new double[] {}), "", "Tweet", source);
	}
	
	@Override
	public String toString() {
		//Just returns the Json of this instance
		return (String) source;
	}
}
