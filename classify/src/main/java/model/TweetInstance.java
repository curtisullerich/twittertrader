package model;

import cc.mallet.types.Instance;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

	public boolean setCompanyTarget() {
		return setTheTarget("company");
	}

	public boolean setSentimentTarget() {
		return setTheTarget("sentiment");
	}

	private boolean setTheTarget(String elem) {
		JsonElement src = (JsonElement) this.getSource();
		JsonObject o = src.getAsJsonObject();
		JsonElement target = o.get(elem);
		JsonElement confidence = o.get(elem + "Confidence");
		if (target != null && confidence != null) {
			double conf = confidence.getAsDouble();
			String targ = target.getAsString();
			if (!(conf < 2)) {
				this.setTheTarget(targ);
				return true;
			} else {
				System.out.println("Attempted to set " + elem
						+ " target from non-verified tweet!");
				System.out.println(targ + " (" + conf + ") " + o.get("text"));
			}
		}
		return false;
	}

	@Override
	public String toString() {
		// Just returns the Json of this instance
		return ((JsonElement) source).getAsString();
	}
}
