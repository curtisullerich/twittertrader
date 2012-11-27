package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;
import dualist.classify.NaiveBayesWithPriorsTrainer;

public class ModelFactory {

	/**
	 * A Method to get a newly trained Company Classifier
	 * @return
	 * 			The classifier trained on companies
	 * @throws FileNotFoundException
	 */
	public static Classifier getCompanyClassifier()
			throws FileNotFoundException {
		InstanceList instances = new InstanceList(PipeFactory.getPipe4());
		File file = new File("../corpus/companyCorpus.txt");
		CsvIterator reader = new CsvIterator(new FileReader(file),
				Constants.CSV_ITERATOR_REGEX, 3, 2, 1);
		instances.addThruPipe(reader);
		MaxEntTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(instances);
		return classifier;
	}
	
	public static Classifier getBackgroundKnowledgeClassifier() {
		ClassifierTrainer<NaiveBayes> n = new NaiveBayesWithPriorsTrainer();
		
		return null;
	}
	
	/**
	 * A Method to get the best available sentiment classifier
	 * @return
	 * 			The classifier trained on sentiment
	 * @throws FileNotFoundException
	 */
	public static Classifier getBestSentimentClassifier()
			throws FileNotFoundException {
		InstanceList instances = new InstanceList(PipeFactory.getPipe4());
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

}
