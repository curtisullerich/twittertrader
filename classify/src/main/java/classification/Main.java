package classification;

import java.io.IOException;

import cc.mallet.classify.Classifier;

public class Main {
	public static void main(String... args) throws IOException {
			ModelUpdater m = new ModelUpdater();
			Classifier c = m.createNewCompanyWikipediaModel();
			
	}
}
