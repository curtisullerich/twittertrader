package pipe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

/**
 * Source: http://comments.gmane.org/gmane.comp.ai.mallet.devel/1724
 * 
 */
public class TwitterFeatures extends Pipe {

	private static final long serialVersionUID = 864155191114123362L;

	public Instance pipe(Instance carrier) {
		// pipe.add(new
		// CharSequenceReplace(Pattern.compile("http\\:\\/\\/.*\\b",
		// Pattern.CASE_INSENSITIVE), "HTTPLINK"));
		// pipe.add( new
		// CharSequenceReplace(Pattern.compile("\\@[\\p{L}\\p{Mn}]+",
		// Pattern.CASE_INSENSITIVE), "@USERLINK"));
		// pipe.add(new CharSequenceReplace(Pattern.compile("\\'"), ""));
		// pipe.add(new CharSequenceReplace(Pattern.compile("\\!\\!+"), "!!"));
		// pipe.add(new CharSequenceReplace(Pattern.compile("\\?\\?+"), "??"));
		String car = (String) carrier.getData();
		Pattern apple = Pattern.compile("@apple", Pattern.CASE_INSENSITIVE);
		Matcher m = apple.matcher(car);
		car = m.replaceAll("NEWSTRINGPATTERN");
		Pattern at = Pattern.compile("\\@[\\p{L}\\p{Mn}]+",
				Pattern.CASE_INSENSITIVE);
		car = at.matcher(car).replaceAll("ATREFERENCE");
		car = car.replaceAll("NEWSTRINGPATTERN", "@apple");

		Pattern apple2 = Pattern.compile("#apple", Pattern.CASE_INSENSITIVE);
		Matcher m2 = apple2.matcher(car);
		car = m2.replaceAll("NEWSTRINGPATTERN");
		Pattern at2 = Pattern.compile("\\#[\\p{L}\\p{Mn}]+",
				Pattern.CASE_INSENSITIVE);
		car = at2.matcher(car).replaceAll("ATREFERENCE");
		car = car.replaceAll("NEWSTRINGPATTERN", "#apple");

		return carrier;
	}
}
