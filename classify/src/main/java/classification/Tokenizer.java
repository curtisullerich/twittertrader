package classification;

import java.util.HashMap;
import java.util.Map;
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
	private static final String emoticons = "([<>]?[:;=8][\\-o\\*\\']?[\\)\\]\\(\\[dDpP/\\:\\}\\{@\\|\\\\]|[\\)\\]\\(\\[dDP/\\:\\}\\{@\\|\\\\][\\-o\\*\\']?[:;=8][<>]?)";
	private static final String usernames = "@[\\w_]+";
	private static final String hashtags = "\\#[\\w_]+[\\w\\'_\\-]*[\\w_]+";

	// 1. finds emoticons
	// 2. finds usernames -> adds them lowercased
	// 3. finds hashtags -> adds them lowercased
	// 4. split apart repeated ? and !
	// 5. normalize repetitions of alpha chars and periods
	//

	public Instance pipe(Instance carrier) {
		TokenSequence ts = new TokenSequence();

		String str = (String) carrier.getData();
		// tokenize(str, ts);
		System.out.println(str);
		Map<Integer, String> matches = new HashMap<Integer, String>();
		Map<String, Integer> unmatchedAfterEmoticons = new HashMap<String, Integer>();// offset,

		Pattern p = Pattern.compile(emoticons);
		Matcher m = p.matcher(str);
		int earliestUnmatched = 0;
		int prevEnd = earliestUnmatched;
		while (m.find()) {
			matches.put(m.start(), m.group());
			unmatchedAfterEmoticons.put(str.substring(prevEnd, m.start()),
					m.start());
			prevEnd = m.end();
		}
		if (prevEnd == 0) {
			unmatchedAfterEmoticons.put(str, 0);
		} else {
			unmatchedAfterEmoticons.put(str.substring(prevEnd), prevEnd);
		}

		Map<String, Integer> unmatchedAfterUsernames = new HashMap<String, Integer>();// offset,
		Pattern userPattern = Pattern.compile(usernames);
		for (String s : unmatchedAfterEmoticons.keySet()) {
			Matcher userMatcher = userPattern.matcher(s);
			prevEnd = 0;
			while (userMatcher.find()) {
				matches.put(
						userMatcher.start() + unmatchedAfterEmoticons.get(s),
						userMatcher.group().toLowerCase());
				unmatchedAfterUsernames.put(
						str.substring(prevEnd, userMatcher.start()),
						userMatcher.start());
				prevEnd = userMatcher.end();
			}
			if (prevEnd == 0) {
				unmatchedAfterUsernames.put(str, 0);
			} else {
				unmatchedAfterUsernames.put(str.substring(prevEnd), prevEnd);
			}
		}

		Map<String, Integer> unmatchedAfterHashtags = new HashMap<String, Integer>();// offset,
		Pattern hashtagPattern = Pattern.compile(hashtags);
		for (String s : unmatchedAfterUsernames.keySet()) {
			Matcher hashtagMatcher = hashtagPattern.matcher(s);
			prevEnd = 0;
			while (hashtagMatcher.find()) {
				matches.put(
						hashtagMatcher.start() + unmatchedAfterUsernames.get(s),
						hashtagMatcher.group().toLowerCase());
				unmatchedAfterHashtags.put(
						str.substring(prevEnd, hashtagMatcher.start()),
						hashtagMatcher.start());
				prevEnd = hashtagMatcher.end();
			}
			if (prevEnd == 0) {
				unmatchedAfterHashtags.put(str, 0);
			} else {
				unmatchedAfterHashtags.put(str.substring(prevEnd), prevEnd);
			}
		}

		for (String s : unmatchedAfterHashtags.keySet()) {
			int offset = unmatchedAfterHashtags.get(s);
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				if (c == '!' || c == '?') {
					matches.put(i + offset, c + "");
				}
			}
			String t = s.replaceAll("[?!]", " ");
			String split[] = t.split("\\s+");
			for (int j = 0; j < split.length; j++) {
				if (split[j].equals(split[j].toUpperCase())) {
					matches.put(offset + j,
							split[j].replaceAll("([A-Z])\\1{3,}", "$1$1$1")
									.replaceAll("\\.{2,}", " ... "));
				} else {
					matches.put(
							offset + j,
							split[j].toLowerCase()
									.replaceAll("([a-z])\\1{3,}", "$1$1$1")
									.replaceAll("\\.{2,}", " ... "));
				}
			}
		}

		int found = 0;
		int k = 0;
		while (found < matches.size()) {
			if (matches.containsKey(k)) {
				String mat = matches.get(k);
				System.out.print(mat + " ");
				ts.add(mat);
				found++;
			}
			k++;
		}
		System.out.println();
		System.out
				.println("---------------------------------------------------------");
		carrier.setData(ts);
		return carrier;
	}

}
