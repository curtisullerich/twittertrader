package common;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import classification.Stemmer;

public class PipeFactory {

	// create a new serial pipes with varied characteristics
	public static SerialPipes getPipe() {
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

	public static SerialPipes getPipe1() {
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

	public static SerialPipes getPipe2() {
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

	public static SerialPipes getPipe3() {
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

	public static SerialPipes getDefault() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		pipe.add(new CharSequenceLowercase());
		// Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		// pipe.add(new
		// CharSequenceReplace(Pattern.compile("http\\:\\/\\/.*\\b",
		// Pattern.CASE_INSENSITIVE), "HTTPLINK"));
		// pipe.add( new
		// CharSequenceReplace(Pattern.compile("\\@[\\p{L}\\p{Mn}]+",
		// Pattern.CASE_INSENSITIVE), "@USERLINK"));
		// pipe.add(new CharSequenceReplace(Pattern.compile("\\'"), ""));
		// pipe.add(new CharSequenceReplace(Pattern.compile("\\!\\!+"), "!!"));
		// pipe.add(new CharSequenceReplace(Pattern.compile("\\?\\?+"), "??"));
//		pipe.add(new TwitterFeatures());
//		pipe.add(new Link2Title());

		Pattern tokenPattern = Pattern.compile("[^\\s]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceRemoveStopwords());
		pipe.add(new Stemmer());
		int[] sizes = { 1, 2 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}
	public static SerialPipes getDansPipes() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		
//		pipe.add(new Emotes());
		
//		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		Pattern tokenPattern = Pattern.compile("[^\\s]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceRemoveStopwords());
 		pipe.add(new Stemmer());
		int[] sizes = {1, 2, 3};
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		//pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}
	
	public static SerialPipes brandonsGetPipes() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		// pipe.add(new TweetPipe());
		pipe.add(new Input2CharSequence("UTF-8"));
		// pipe.add(new SpellCheck());
		// Stop words
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		// pipe.add(new TokenSequenceRemoveStopwords(new
		// File("../Stopwords.txt"), "UTF-8", false, false, false));
		pipe.add(new TokenSequenceRemoveStopwords());
		// Stemming
		pipe.add(new Stemmer());
		int[] sizes = { 1, 2 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	public static SerialPipes getPipe5() {
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

	public static SerialPipes getPipe6() {
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

	public static SerialPipes getPipe7() {
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

	public static SerialPipes getPipe8() {
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
}
