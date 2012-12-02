package pipe;

import org.apache.commons.lang.StringEscapeUtils;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

/**
 * Replaces tokens like &gt; with >, etc.
 */
public class UnescapeHTML extends Pipe {

	private static final long serialVersionUID = 1013696874537828543L;
	private static final String emoticons = "(?:" + "[<>]?" + "[:;=8]"
			+ "[\\-o\\*\\']?" + "[\\)\\]\\(\\[dDpP/\\:\\}\\{@\\|\\]" + "|"
			+ "[\\)\\]\\(\\[dDpP/\\:\\}\\{@\\|\\]" + "[\\-o\\*\\']?" + "[:;=8]"
			+ "[<>]?" + ")";

	public Instance pipe(Instance carrier) {
		String str = (String) carrier.getData();
		// System.out.println("----------------------------------------------");
		// System.out.println("Before UnescapeHTML: " + str);
		String str2 = StringEscapeUtils.unescapeHtml(str);
		if (!str2.equals(str)) {
			// System.out.println(str);
			// System.out.println(str2);
			// System.out.println("----------------");
			carrier.setData(str2);
		}
		// str2 = str2.replaceAll("([A-Za-z])\\1{3,}", "$1$1$1").replaceAll(
		// "\\.{2,}", " ... ");
		//
		// String split[] = str2.split("\\s+");
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < split.length; i++) {
		// if (split[i].equals(split[i].toUpperCase())) {
		// sb.append(split[i]);
		// } else if (!split[i].contains("http://") &&
		// !split[i].equalsIgnoreCase("apple")) {
		// sb.append(split[i].toLowerCase());
		// } else {
		// sb.append(split[i]);
		// }
		// sb.append(" ");
		// }
		// carrier.setData(sb.toString());
		// System.out.println("After UnescapeHTML: " + sb);
		return carrier;
	}
}
