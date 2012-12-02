package pipe;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

import common.Constants;

public class Link2Title extends Pipe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6856138170442079105L;

	/**
	 * This assumes that the carrier still has the Tweet Text STRING in the data
	 * field
	 */
	@Override
	public Instance pipe(Instance carrier) {
		Pattern httpPattern = Pattern.compile(Constants.HTTP_REGEX);

		String text = (String) carrier.getData();

		if (text == null) {
			return carrier;
		}

		Matcher m = httpPattern.matcher(text);

		StringBuilder sb = new StringBuilder();

		/*
		 * We assume that the tweet uses at least somewhat proper English and
		 * has spaces between each word. That way we can just look at each word,
		 * see if it's a link, look it up if it is, otherwise just append the
		 * string, and move on.
		 */

		for (String s : text.split("\\s+")) {
			// We found a link so look it up
			if (s.matches(Constants.HTTP_REGEX) && m.find()) {
				String url = m.group();
				Document doc = null;
				try {
					doc = Jsoup.connect(url).get();
					if (doc.title() != null && !doc.title().equals("")) {
						// Flawless victory!
						sb.append(doc.title());
						System.out.println("replaced " + url + " with "
								+ doc.title());
					} else {
						// try to resolve the url
						sb.append(doc.baseUri());
						System.out.println("replaced " + url + " with "
								+ doc.baseUri());
					}
				} catch (IOException e) {
					// Ignore it and put the url back into the string
					sb.append(url);
					System.out.println("did not replace url " + url);
				}
			}
			// Regular word so just throw it into the result
			else {
				sb.append(s);
			}
			sb.append(" ");
		}

		System.out.println("After replacing: " + sb.toString());
		carrier.setData(sb.toString());
		return carrier;
	}
}
