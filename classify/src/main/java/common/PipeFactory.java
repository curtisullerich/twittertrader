package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceNGrams;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import classification.Stemmer;
import classification.Tokenizer;
import classification.UnescapeHTML;

import com.google.common.collect.Iterators;

public class PipeFactory {

	public static Iterator<SerialPipes> getPipes() {
		return new SetCombiner<SerialPipes, Pipe>() {

			@Override
			public SerialPipes combine(Iterable<Pipe> in) {
				return new SerialPipes(Iterators.toArray(in.iterator(),
						Pipe.class));
			}

		}.add(new SetFactory<Pipe>() {

			@Override
			public Iterable<Pipe> build() {
				ArrayList<Pipe> pipes = new ArrayList<Pipe>();
				pipes.add(new Input2CharSequence("UTF-8"));
				pipes.add(new UnescapeHTML());
				pipes.add(new CharSequenceLowercase());
				return pipes;
			}

		}).add(new SetFactory<Pipe>() {

			@Override
			public Iterable<Pipe> build() {

				return Arrays.asList(new Pipe[] {
						new CharSequence2TokenSequence(Pattern
								.compile("[\\p{L}\\p{N}_]+")),
						new CharSequence2TokenSequence(Pattern
								.compile("[^\\s]+")) });
			}

		}).iterator();
	}

	public static SerialPipes getDefault() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		pipe.add(new UnescapeHTML());
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
		// pipe.add(new TwitterFeatures());
		// pipe.add(new Link2Title());

		Pattern tokenPattern = Pattern.compile("[^\\s]+");
		// pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new Tokenizer());
		pipe.add(new TokenSequenceRemoveStopwords());
		pipe.add(new Stemmer());
		int[] sizes = { 1, 2 };
		// pipe.add(new PrintTokenSequenceFeatures());
		// pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipe.add(new PrintInputAndTarget());
		return new SerialPipes(pipe);
	}

	public static SerialPipes getDansPipes() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));

		// pipe.add(new Emotes());

		// Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		Pattern tokenPattern = Pattern.compile("[^\\s]+");
		pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new TokenSequenceRemoveStopwords());
		pipe.add(new Stemmer());
		int[] sizes = { 1, 2, 3 };
		pipe.add(new TokenSequenceNGrams(sizes));
		pipe.add(new TokenSequence2FeatureSequence());
		pipe.add(new Target2Label());
		pipe.add(new FeatureSequence2FeatureVector());
		// pipeList.add(new PrintInputAndTarget());
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
}
