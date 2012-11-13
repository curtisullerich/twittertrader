package classification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;

public class ModelTester {
	public static void main(String[] args) throws IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		String stamp = sdf.format(date);

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				new File("../companyModel" + stamp + ".mallet")));
		oos.writeObject(getCompanyClassifier());
		oos.close();
		ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(
				new File("../sentimentModel" + stamp + ".mallet")));
		oos2.writeObject(getBestSentimentClassifier());
		oos2.close();

		System.exit(1);

		ArrayList<SerialPipes> allPipes = new ArrayList<SerialPipes>();
		// add all the pipe variations to the list
		// allPipes.add(getPipe());
		// allPipes.add(getPipe1());
		// allPipes.add(getPipe2());
		// allPipes.add(getPipe3());
		allPipes.add(getPipe4());
		// allPipes.add(getPipe5());
		// allPipes.add(getPipe6());
		// allPipes.add(getPipe7());
		// allPipes.add(getPipe8());

		ArrayList<Trial> trials = new ArrayList<Trial>();
		ArrayList<String> info = new ArrayList<String>();

		for (SerialPipes pipe : allPipes) {
			InstanceList instances = new InstanceList(pipe);
			// CsvIterator reader = new CsvIterator(new FileReader(new File(
			// "tweets.txt")), "(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
			File file = new File("../corpus/sentiment.txt");
			File file2 = new File("../corpus/tweets.txt");

			CsvIterator reader = new CsvIterator(new FileReader(file),
					"(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
			instances.addThruPipe(reader);
			CsvIterator reader2 = new CsvIterator(new FileReader(file2),
					"(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
			instances.addThruPipe(reader2);

			InstanceList[] instanceLists = instances.split(new Randoms(),
					new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
							0.1 });

			String description = "";
			for (Pipe p : pipe.pipes()) {
				description += p.getClass().getName() + " ";
			}
			info.add(description);

			for (ClassifierTrainer<? extends Classifier> trainer : trainers()) {
				double accuracies = 0;
				double rankings = 0;
				for (int i = 0; i < 10; i++) {
					Classifier classifier = trainer.train(getAllButIndex(
							instanceLists, i));

					Trial trial = new Trial(classifier, instanceLists[i]);
					accuracies += trial.getAccuracy();
					rankings += trial.getAverageRank();
					trials.add(trial);
					if (i == 0) {
						info.add(trial.getClassifier().getClass().getName());
					}
				}
				info.add("Average accuracy: " + accuracies / 10);
				info.add("Average ranking: " + rankings / 10);
				info.add("---");
			}
			info.add("~~~~~~~~~");
		}

		for (String s : info) {
			System.out.println(s);
		}

		for (Trial trial : trials) {
			System.out.println(trial.getClassifier().getClass().getName());
			System.out.println("Accuracy: " + trial.getAccuracy());
			System.out.println("Average rank: " + trial.getAverageRank());
			System.out.println("~~~~~~~~");
		}
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
		InstanceList instances = new InstanceList(getPipe4());
		File file = new File("../corpus/sentiment.txt");
		File file2 = new File("../corpus/tweets.txt");

		CsvIterator reader = new CsvIterator(new FileReader(file),
				"(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
		instances.addThruPipe(reader);
		CsvIterator reader2 = new CsvIterator(new FileReader(file2),
				"(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
		instances.addThruPipe(reader2);
		MaxEntTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(instances);
		return classifier;
	}

	public static Classifier getCompanyClassifier()
			throws FileNotFoundException {
		InstanceList instances = new InstanceList(getPipe4());
		File file = new File("../corpus/companyCorpus.txt");
		CsvIterator reader = new CsvIterator(new FileReader(file),
				"(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
		instances.addThruPipe(reader);
		MaxEntTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(instances);
		return classifier;
	}

	// create a bunch of trainers
	private static ArrayList<ClassifierTrainer<? extends Classifier>> trainers() {
		ArrayList<ClassifierTrainer<? extends Classifier>> trainers = new ArrayList<ClassifierTrainer<? extends Classifier>>();
		trainers.add(new MaxEntTrainer());
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
		// trawiners.add(new NaiveBayesEMTrainer());
		// trainers.add(new WinnowTrainer());
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

	// create a new serial pipes with varied characteristics
	private static SerialPipes getPipe() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceLowercase());
		int[] sizes = { 1, 2, 3, 4, 5 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe1() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceLowercase());
		int[] sizes = { 1, 2 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe2() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceLowercase());
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe3() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceLowercase());
		int[] sizes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	static SerialPipes getPipe4() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
//			pipe.add(new TweetPipe());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		int[] sizes = { 1, 2, 3, 4, 5 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe5() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		int[] sizes = { 1, 2 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe6() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe7() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		int[] sizes = { 1, 2, 3, 4, 5 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	private static SerialPipes getPipe8() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		int[] sizes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
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