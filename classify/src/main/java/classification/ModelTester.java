package classification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.InstanceList;

import common.Constants;
import common.PipeFactory;
import common.SetItem;

public class ModelTester {

	private static final int random_seed = 0xDEADBEEF;

	private static final int folds = 6;
	
	private static final int test_size = 200;

	public static void main(String[] args) throws IOException {
		
		int d = 40;
		int max = 1000;
		
		ArrayList<String> info = new ArrayList<String>();
		
		for(int p=d; p<=max; p+=d) {
			List<String> r = run(p);
			for(String s : r)
				info.add(String.valueOf(p) + " - " + s);
		}
		
		for (String s : info) {
			System.out.println(s);
		}
	}

	private static List<String> run(int count) throws FileNotFoundException {

		Iterator<SetItem<SerialPipes>> pipesIterator = PipeFactory.getPipes();

		ArrayList<String> info = new ArrayList<String>();

		while (pipesIterator.hasNext()) {
			SetItem<SerialPipes> n = pipesIterator.next();
			SerialPipes pipe = n.value;
			String pipeLabel = n.label;

			ArrayList<Trial> trials = new ArrayList<Trial>();

			info.add(pipeLabel);

			InstanceList instances = new InstanceList(pipe);
			File file = new File("../corpus/appleBinaryFiltered.txt");
			CsvIterator reader = new CsvIterator(new FileReader(file),
					Constants.CSV_ITERATOR_REGEX, 3, 1, 2);
			instances.addThruPipe(reader);
			
			
			InstanceList[] testSplit = instances.splitInOrder(new int[] {test_size, instances.size()-test_size});
			InstanceList testList = testSplit[0];
			InstanceList trainList = testSplit[1];

			// Filter instances
			//double proportion = (double)count / trainList.size();
			InstanceList[] randomInstanceLists = trainList.splitInOrder(new int[] {count, trainList.size()-count});
//					.split(new Random(random_seed), new double[] { proportion,
//							1.0 - proportion });

			CrossValidationIterator cvi = new CrossValidationIterator(
					randomInstanceLists[0], folds, new Random(random_seed));

//			String description = "";
//			for (Pipe p : pipe.pipes()) {
//				description += p.getClass().getName() + " ";
//			}
//			info.add(description);

			for (ClassifierTrainer<? extends Classifier> trainer : trainers()) {
				double accuracies = 0;
				double rankings = 0;

				int i = 0;
				while (cvi.hasNext()) {
					i++;
					InstanceList[] instanceLists = cvi.next();
					InstanceList train = instanceLists[0];
					//InstanceList test = instanceLists[1];
					InstanceList test = testList;

					Classifier classifier = trainer.train(train);

//					System.err.println(((NaiveBayes) classifier).getPriors());
//
//					((NaiveBayes) classifier).printWords(30);

					Trial trial = new Trial(classifier, test);

//					System.err.println(trial.getAccuracy());
//					System.err.println(classifier.getLabelAlphabet());
					accuracies += trial.getAccuracy();
					rankings += trial.getAverageRank();
					trials.add(trial);
				}
				info.add("Average accuracy: " + accuracies / i);
				// info.add("Average ranking: " + rankings / 10);
				// info.add("---");
			}
			info.add("~~~~~~~~~");

		}

//		for (String s : info) {
//			System.out.println(s);
//		}
		return info;
	}

	public static void jsonTest() throws FileNotFoundException, IOException,
			ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				"../companyModel20121111040918.mallet"));
		Classifier classifier = (Classifier) ois.readObject();
		ois.close();

	}

	public static Classifier getBestSentimentClassifier()
			throws FileNotFoundException {
		InstanceList instances = new InstanceList(PipeFactory.getDefault());
		File file = new File("../corpus/sentiment.txt");
		File file2 = new File("../corpus/tweets.txt");

		CsvIterator reader = new CsvIterator(new FileReader(file),
				Constants.CSV_ITERATOR_REGEX, 3, 2, 1);
		instances.addThruPipe(reader);
		CsvIterator reader2 = new CsvIterator(new FileReader(file2),
				Constants.CSV_ITERATOR_REGEX, 3, 2, 1);
		instances.addThruPipe(reader2);
		MaxEntTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(instances);
		return classifier;
	}

	public static Classifier getCompanyClassifier()
			throws FileNotFoundException {
		InstanceList instances = new InstanceList(PipeFactory.getDefault());
		File file = new File("../corpus/companyset.txt");
		CsvIterator reader = new CsvIterator(new FileReader(file),
				Constants.CSV_ITERATOR_REGEX, 3, 2, 1);
		instances.addThruPipe(reader);
		MaxEntTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(instances);
		return classifier;
	}

	// create a bunch of trainers
	private static ArrayList<ClassifierTrainer<? extends Classifier>> trainers() {
		ArrayList<ClassifierTrainer<? extends Classifier>> trainers = new ArrayList<ClassifierTrainer<? extends Classifier>>();
		trainers.add(new NaiveBayesTrainer());
		// trainers.add(new NaiveBayesTrainer());
		// ~ trainers.add(new AdaBoostM2Trainer());
		// ~ trainers.add(new AdaBoostTrainer());
		// ~ trainers.add(new BaggingTrainer());
		// trainers.add(new BalancedWinnowTrainer());
		// -- trainers.add(new C45Trainer());
		// ~ trainers.add(new ClassifierEnsembleTrainer());
		// ~ trainers.add(new ConfidencePredictingClassifierTrainer());
		// -- trainers.add(new MaxEntGETrainer());
		// -- trainers.add(new MaxEntPRTrainer());
		// trainers.add(new MCMaxEntTrainer());
		// trainers.add(new NaiveBayesEMTrainer());
		// trainers.add(new WinnowTrainer());
		trainers.add(new MaxEntTrainer());
		return trainers;
	}

	private static InstanceList getAllButIndex(InstanceList[] lists, int i) {
		InstanceList copy = lists[0].cloneEmpty();// create a new one
		for (int j = 0; i < lists.length; i++) {
			if (j != i) {
				// add all but one (9 of the 10) to the new list
				copy.addAll(lists[i]);
			}
		}
		return copy;
	}

	public static File convert() throws IOException {
		Scanner s = new Scanner(new File(
				"/home/curtis/git/twittertrader/corpus/tweets.csv"));
		File file = new File("/home/curtis/git/twittertrader/corpus/tweets.txt");
		FileWriter f = new FileWriter(file);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] t = line.split("\",\"");
			if (t[0].equals("0")) {
				String next = "testing negative " + t[5] + "\n";
				f.write(next);
			} else if (t[0].equals("2")) {
				f.write("testing neutral " + t[5] + "\n");
			} else if (t[0].equals("4")) {
				f.write("testing positive " + t[5] + "\n");
			}
		}
		f.flush();
		f.close();
		return file;
	}

	public static File convert2() throws IOException {
		Scanner s = new Scanner(new File(
				"/home/curtis/git/twittertrader/corpus/sentiment.csv"));
		File file = new File(
				"/home/curtis/git/twittertrader/corpus/sentiment.txt");
		FileWriter f = new FileWriter(file);
		while (s.hasNextLine()) {
			String line = s.nextLine();
			String[] t = line.split("\",\"");
			String text = "testing " + t[0] + " " + t[t.length - 1] + "\n";
			if (t[0].equals("positive") || t[0].equals("negative")
					|| t[0].equals("neutral") || t[0].equals("irrelevant")) {
				f.write(text);

			}
			// if (t[0].equals("0")) {
			// String next ="testing negative " + t[5] + "\n";
			// f.write(next);
			// } else if (t[0].equals("2")) {
			// f.write("testing neutral " + t[5] + "\n");
			// } else if (t[0].equals("4")) {
			// f.write("testing positive " + t[5] + "\n");
			// }
		}
		f.flush();
		f.close();
		return file;
	}

}