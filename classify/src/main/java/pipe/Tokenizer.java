package pipe;

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
	private static final String links = "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))(:[\\d]{1,5})?(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b"; // "http\\:\\/\\/.*?\\s";

	private static final String specialcases = "(\\.\\.\\.)|([\\.0oO\\*-\\^]_+[\\.0oO\\*-\\^])";// \\.0oO\\*@-\\^

	// 1. finds emoticons
	// 2. finds usernames -> adds them lowercased
	// 3. finds hashtags -> adds them lowercased
	// 4. normalize repetitions of all characters
	// 5. maintain capitalization of "apple"
	// 6. remove commas, colons, semicolons, hyphens?
	// possible remove RT and -
	public Tokenizer() {
		this(true, true, true, true, true, true, true, true, true, Case.apple);
	}

	public Tokenizer(boolean print) {
		this(true, true, true, true, true, true, true, true, print, Case.apple);

	}

	public Tokenizer(boolean doEmoticons, boolean doUsernames,
			boolean doHashtags, boolean doLinks, boolean doSpecialCases,
			boolean removeRT, boolean normalizeLengths, boolean removePunct,
			boolean print, Case casing) {
		this.doEmoticons = doEmoticons;
		this.doUsernames = doUsernames;
		this.doHashtags = doHashtags;
		this.doLinks = doLinks;
		this.doSpecialCases = doSpecialCases;
		this.removeRT = removeRT;
		this.normalizeLengths = normalizeLengths;
		this.removePunct = removePunct;
		this.casing = casing;
		this.print = print;

	}

	private boolean doEmoticons;
	private boolean doUsernames;
	private boolean doHashtags;
	private boolean doLinks;
	private boolean doSpecialCases;
	private boolean removeRT;
	private boolean normalizeLengths;
	private boolean removePunct;// removes any non-word characters from edges of
															// space-delimited tokens
	private boolean print;
	private Case casing;

	public enum Case {
		lower, nochange, apple;
	}

	private List<String> match(String regex, TokenSequence ts, String str,
			boolean toLowercase, String replacement) {
		List<String> ret = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		int prevEnd = 0;
		while (m.find()) {
			String s = m.group();
			if (toLowercase) {
				s = s.toLowerCase();
			}
			if (print) {
				System.out.println(s);
			}
			ts.add(replacement == null ? s : replacement);
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
		if (print) {
			System.out.println(str);
		}
		if (removeRT) {
			str = str.replaceAll("( RT )(^RT)( RT:)", "");
		}
		if (normalizeLengths) {
			str = str.replaceAll("\\.{2,}", " ... ");
		}
		List<String> strings1 = new ArrayList<String>();
		List<String> strings2 = new ArrayList<String>();
		boolean use1 = true;
		strings1.add(str);

		if (doSpecialCases) {
			use1 = next(strings1, strings2, use1, specialcases, ts, false, null);
		}

		if (doLinks) {
			use1 = next(strings1, strings2, use1, links, ts, false, null);
		}
		if (doEmoticons) {
			use1 = next(strings1, strings2, use1, emoticons, ts, false, null);
		}

		if (doUsernames) {
			use1 = next(strings1, strings2, use1, usernames, ts, true, null);
		}
		if (doHashtags) {
			use1 = next(strings1, strings2, use1, hashtags, ts, true, null);
		}

		for (String s : use1 ? strings1 : strings2) {
			String spl[] = s.split("\\s+");
			for (String ss : spl) {

				switch (casing) {
				case apple:
					if (!ss.toLowerCase().contains("apple")) { // !ss.equals(ss.toUpperCase()))
						// {
						ss = ss.toLowerCase();
					}
					break;
				case lower:
					ss = ss.toLowerCase();
					break;
				case nochange:
				default:
					break;
				}
				if (normalizeLengths) {

					ss = ss.replaceAll("([A-Za-z])\\1{3,}", "$1$1$1");
				}
				if (removePunct) {
					ss = ss.replaceAll("^[^\\w$]+", "");
					ss = ss.replaceAll("[^\\w%]+$", "");
				}

				if (ss.length() > 0) {
					ts.add(ss);
					if (print) {
						System.out.println(ss);
					}
				} else {
					// System.out.println("!!!!!!!!!!!!!!!Didn't add supposedly empty string3: "
					// + ss);
				}
			}
		}
		if (print) {
			System.out.println("---------------------------------------------");
		}
		carrier.setData(ts);
		return carrier;
	}

	private boolean next(List<String> remaining1, List<String> remaining2,
			boolean is1, String regex, TokenSequence ts, boolean toLowercase,
			String replacement) {
		if (is1) {
			for (String s : remaining1) {
				remaining2.addAll(match(regex, ts, s, toLowercase, replacement));
			}
			remaining1.clear();
		} else {
			for (String s : remaining2) {
				remaining1.addAll(match(regex, ts, s, toLowercase, replacement));

			}
			remaining2.clear();
		}
		return !is1;
	}

}