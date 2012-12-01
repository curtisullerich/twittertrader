package classification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.TokenSequence;

/**
 * Custom tokenizer.
 */
public class Tokenizer extends Pipe {

	private static final long serialVersionUID = 1013696874537828543L;
	// "-_+-
	private static final String emoticons = "([<>]?[:;=8][\\-o\\*\\']?[\\)\\]\\(\\[dDpP/\\:\\}\\{@\\|\\\\]|[\\)\\]\\(\\[dDP/\\:\\}\\{@\\|\\\\][\\-o\\*\\']?[:;=8][<>]?)";
	private static final String usernames = "@[\\w_]+";
	private static final String hashtags = "\\#[\\w_]+[\\w\\'_\\-]*[\\w_]+";
	private static final String links = "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
			+ "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
			+ "|mil|biz|info|mobi|name|aero|jobs|museum"
			+ "|travel|[a-z]{2}))(:[\\d]{1,5})?"
			+ "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
			+ "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
			+ "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
			+ "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
			+ "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
			+ "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b"; // "http\\:\\/\\/.*?\\s";

	// 1. finds emoticons
	// 2. finds usernames -> adds them lowercased
	// 3. finds hashtags -> adds them lowercased
	// 4. normalize repetitions of all characters
	// 5. maintain capitalization of "apple"
	// 6. remove commas, colons, semicolons, hyphens?
	// possible remove RT and -

	private List<String> match(String regex, TokenSequence ts, String str,
			boolean toLowercase) {
		List<String> ret = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		int prevEnd = 0;
		while (m.find()) {
			String s = m.group();
			if (toLowercase) {
				s = s.toLowerCase();
			}
			System.out.println(s);
			ts.add(s);
			if (m.start() >= prevEnd) {
				ret.add(str.substring(prevEnd, m.start()));
			} else {
				// System.out.println("!!!!!!!!!!!!!!!Didn't add supposedly empty string1: "
				// + s + " with prevEnd=" + prevEnd + " and m.start=" +
				// m.start());
			}
			prevEnd = m.end();
		}
		if (str.length() > prevEnd) {
			ret.add(str.substring(prevEnd));
		} else {
			// System.out.println("!!!!!!!!!!!!!!!Didn't add supposedly empty string2: "
			// + str.substring(prevEnd));

		}
		return ret;
	}

	public Instance pipe(Instance carrier) {
		TokenSequence ts = new TokenSequence();

		String str = (String) carrier.getData();
		// tokenize(str, ts);
		System.out.println(str);

		List<String> superA = match(links, ts, str, false);

		List<String> a = new ArrayList<String>();
		for (String s : superA) {
			a.addAll(match(emoticons, ts, s, false));
		}

		List<String> b = new ArrayList<String>();
		for (String s : a) {
			b.addAll(match(usernames, ts, s, true));
		}
		List<String> c = new ArrayList<String>();
		for (String s : b) {
			c.addAll(match(hashtags, ts, s, true));
		}

		List<String> d = new ArrayList<String>();
		for (String s : c) {
			s = s.replaceAll("\\.{2,}", " ... ");
			String spl[] = s.split("\\s+");
			for (String ss : spl) {
				if (!ss.equals(ss.toUpperCase())) {
					ss = ss.toLowerCase();
				}
				ss = ss.replaceAll("([A-Za-z])\\1{3,}", "$1$1$1");
				ss = ss.replaceAll("^[^\\w]", "");
				ss = ss.replaceAll("[^\\w]$", "");
				if (ss.length() > 0) {
					ts.add(ss);
					System.out.println(ss);
				} else {
					// System.out.println("!!!!!!!!!!!!!!!Didn't add supposedly empty string3: "
					// + ss);
				}
				// d.addAll(match("[^\\s]+",ts,ss));
			}

		}
		// if (!d.isEmpty()) {
		// System.out.print("Still had some tokens remaining! :");
		// for (String s : d) {
		// System.out.print(" " + s);
		// }
		// System.out.println();
		// }
		System.out
				.println("---------------------------------------------------------");
		carrier.setData(ts);
		return carrier;
	}

}
