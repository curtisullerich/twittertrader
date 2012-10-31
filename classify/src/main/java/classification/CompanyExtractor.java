package classification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.types.Label;

public class CompanyExtractor {
	public static void main(String... args) throws IOException,
			ClassNotFoundException {
		// companyCorpus();
		System.exit(0);

		Scanner s = new Scanner(new File("../corpus/random.txt"));
		Map<String, ArrayList<String>> out = new HashMap<String, ArrayList<String>>();
		String[] keywords = { "fareway", "hyvee", "hy-vee", "hy vee",
				"walmart", "wal-mart", "wal mart", "Verizon", "Cerner",
				"Dollar Tree", "Raytheon", "Starbucks", "Xerox", "Costco",
				"Coca Cola", "Coca-cola", "coke", "eBay", "Ford", "Boeing",
				"Monsanto", "Exelon", "GOOG", "Microsoft", "Amazon", "AMZN",
				"Garmin", "GRMN", "Travelzoo", "TZOO", "Netflix", "NFLX",
				"priceline", "INFN", "Zoltek Cos", "zoltek", "ZOLT",
				"First Solar", "FSLR" };

		for (String t : keywords) {
			out.put(t, new ArrayList<String>());
		}

		while (s.hasNextLine()) {
			String next = s.nextLine();
			if (next.startsWith("\"")) {
				for (String t : keywords) {
					if (next.toLowerCase().contains(t.toLowerCase())) {
						ArrayList<String> cur = out.get(t);
						cur.add(next);
						out.put(t, cur);
					}
				}
			}
		}

		for (String t : out.keySet()) {
			System.out.println(t + ": " + out.get(t).size());
			FileWriter writer = new FileWriter(new File("../corpus/companies/"
					+ t + ".csv"));
			for (String u : out.get(t)) {
				writer.append(u + "\n");
			}
			writer.flush();
			writer.close();
		}
	}

	public static void companyCorpus() throws IOException {
		String path = "../localcorpora/companies/sum/uniq/small/25";
		File dir = new File(path);
		FileWriter out = new FileWriter(new File("../corpus/companyCorpus.txt"));
		for (File child : dir.listFiles()) {
			String name = child.getName();
			Scanner s = new Scanner(child);
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split("\",\"");
				String tweet = line[line.length - 1];
				out.append("curtis " + name + " "
						+ tweet.substring(0, tweet.length() - 1) + "\n");
			}

		}
		out.flush();
		out.close();
	}

}
