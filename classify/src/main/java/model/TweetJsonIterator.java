package model;

import java.util.Iterator;

import cc.mallet.types.Instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TweetJsonIterator implements Iterator<Instance> {

	public enum Mode {
		TRAIN, CLASSIFY, TEST;
	}

	public enum Type {
		COMPANY, SENTIMENT, UNCLASSIFIED;
		public String toString() {
			switch (this) {
			case COMPANY:
				return "company";
			case SENTIMENT:
				return "sentiment";
			case UNCLASSIFIED:
				return "unclassified";
			default:
				return "fail";
			}
		}
	}

	Mode mode;
	Type type;

	Iterator<JsonElement> iterator;

	public TweetJsonIterator(String json, Mode mode, Type type) {
		JsonParser parser = new JsonParser();
		JsonElement e = parser.parse(json);
		JsonArray j = e.getAsJsonArray();
		iterator = j.iterator();
		this.mode = mode;
		this.type = type;
	}

	// The PipeInputIterator interface

	public Instance next() {
		JsonElement next = iterator.next();
		// System.out.println(next);
		JsonObject o = next.getAsJsonObject();
		if (o == null) {
			throw new IllegalArgumentException("Null JsonObject");
		}
		JsonElement texto = o.get("text");
		if (texto == null) {
			throw new IllegalArgumentException("Text not present for tweet: "
					+ o);
		}
		String data = texto.getAsString();
		String name = o.get("id_str").getAsString();
		String target = "";
		if (mode != Mode.CLASSIFY) {
			// System.out.println("getting target for type: "
			// + this.type.toString());
			JsonElement targ = o.get(this.type.toString());
			JsonElement confidence = o.get(this.type.toString() + "Confidence");
			if (confidence == null || confidence.getAsDouble() < 2) {
				throw new IllegalArgumentException(
						"Trying to train on an unverified tweet!");
			}
			if (targ != null) {
				target = targ.getAsString();
			} else {
				throw new IllegalArgumentException("No target present.");
			}
		}
		System.out.println("creating new instance: [target]=" + target
				+ " [data]=" + data);
		Instance carrier = new Instance(data, target, name, o);
		return carrier;
	}

	public boolean hasNext() {
		if (!iterator.hasNext()) {
			return false;
		}
		return true;
	}

	public void remove() {
		throw new IllegalStateException(
				"This Iterator<Instance> does not support remove().");
	}

}
