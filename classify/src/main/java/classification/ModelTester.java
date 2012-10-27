package classification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayesTrainer;
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
	public static void main(String[] args) throws FileNotFoundException {
		ArrayList<SerialPipes> allPipes = new ArrayList<SerialPipes>();
		// add all the pipe variations to the list
		allPipes.add(getPipe());

		ArrayList<Trial> trials = new ArrayList<Trial>();
		ArrayList<String> info = new ArrayList<String>();

		for (SerialPipes pipe : allPipes) {
			InstanceList instances = new InstanceList(pipe);
			CsvIterator reader = new CsvIterator(new FileReader(new File(
					"tweets.txt")), "(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);
			instances.addThruPipe(reader);
			InstanceList[] instanceLists = instances.split(new Randoms(),
					new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
							0.1 });

			String description = "";
			for (Pipe p : pipe.pipes()) {
				description += p.getClass().getName() + " ";
			}
			info.add(description);

			for (ClassifierTrainer trainer : trainers()) {
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

	// create a bunch of trainers
	private static ArrayList<ClassifierTrainer> trainers() {
		ArrayList<ClassifierTrainer> trainers = new ArrayList<ClassifierTrainer>();
		trainers.add(new MaxEntTrainer());
		trainers.add(new NaiveBayesTrainer());

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
		int[] sizes = { 1, 2 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}
}