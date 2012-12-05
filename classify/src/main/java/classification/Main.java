package classification;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String... args) throws IOException {

		Scanner s = new Scanner(new File("/home/curtis/git/twittertrader/paper/allsorted"));
		int i = 0;
		while (s.hasNextLine()) {
			String l = s.nextLine();

			String ls[] = l.split("\t");
			if (i == 0) {
				System.out.print(ls[1]);
			} 
			
			//Replace 2 with the index of the field you actually want. You could write them out to individual files if you want as well.
			System.out.print("\t" + ls[2]);
			if (i == 17) {
				if (!ls[0].equals("1800")) {
					System.err.println("\nFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAIL");
					System.exit(1);
				}
				System.out.print("\n");
				i = -1;
			} 

			i++;
		}
		// ModelUpdater m = new ModelUpdater();
		// Classifier c = m.createNewCompanyWikipediaModel();

		// Classifier c =

		// Scanner s = new Scanner(new File("../corpus/companies.txt"));
		// FileWriter f = new FileWriter(new File("../corpus/companyset.txt"));
		// while (s.hasNextLine()) {
		// String l = s.nextLine();
		// String[] split= l.split("\",\"");
		// if (split.length < 10) {
		// System.out.println(l);
		// }
		// String o = split[1] + " train " + split[9] + "\n";
		// f.append(o);
		// }
		// f.flush();
		// f.close();
	}
};
