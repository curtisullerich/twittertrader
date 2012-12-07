package classification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String... args) throws IOException {

		Scanner s = new Scanner(new File(
				"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/allsorted"));
		int i = 0;

		FileWriter accuracyW = new FileWriter(new File(
				"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/accuracy.tsv"));
		FileWriter applePrecisionW = new FileWriter(
				new File(
						"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/applePrecision.tsv"));
		FileWriter nonePrecisionW = new FileWriter(
				new File(
						"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/nonePrecision.tsv"));
		FileWriter appleRecallW = new FileWriter(new File(
				"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/appleRecal.tsv"));
		FileWriter noneRecallW = new FileWriter(new File(
				"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/noneRecall.tsv"));
		FileWriter appleF1W = new FileWriter(new File(
				"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/appleF1.tsv"));
		FileWriter noneF1W = new FileWriter(new File(
				"/home/curtis/Dropbox/Shared Folders/apple/thenewstats2/noneF1.tsv"));

		while (s.hasNextLine()) {
			String l = s.nextLine();

			String ls[] = l.split("\t");
			if (i == 0) {
				System.out.print(ls[1]);
				accuracyW.append(ls[1]);
				applePrecisionW.append(ls[1]);
				nonePrecisionW.append(ls[1]);
				appleRecallW.append(ls[1]);
				noneRecallW.append(ls[1]);
				appleF1W.append(ls[1]);
				noneF1W.append(ls[1]);
			}

			// Replace 2 with the index of the field you actually want. You could
			// write them out to individual files if you want as well.
			System.out.print("\t" + ls[2]);
			accuracyW.append("\t" + ls[2]);
			applePrecisionW.append("\t" + ls[4]);
			nonePrecisionW.append("\t" + ls[5]);
			appleRecallW.append("\t" + ls[6]);
			noneRecallW.append("\t" + ls[7]);
			appleF1W.append("\t" + ls[8]);
			noneF1W.append("\t" + ls[9]);
			if (i == 29) {
				if (!ls[0].equals("1500")) {
					System.err
							.println("\nFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAILFAIL");
					System.exit(1);
				}
				System.out.print("\n");
				accuracyW.append("\n");
				applePrecisionW.append("\n");
				nonePrecisionW.append("\n");
				appleRecallW.append("\n");
				noneRecallW.append("\n");
				appleF1W.append("\n");
				noneF1W.append("\n");
				i = -1;
			}

			i++;
		}
		accuracyW.flush();
		applePrecisionW.flush();
		nonePrecisionW.flush();
		appleRecallW.flush();
		noneRecallW.flush();
		appleF1W.flush();
		noneF1W.flush();
		
		accuracyW.close();
		applePrecisionW.close();
		nonePrecisionW.close();
		appleRecallW.close();
		noneRecallW.close();
		appleF1W.close();
		noneF1W.close();
		
		
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
