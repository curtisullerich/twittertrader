package common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import pipe.Stemmer;
import pipe.Tokenizer;
import pipe.Tokenizer.Case;
import pipe.UnescapeHTML;
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

public class PipeFactory {

	public static Iterator<SetItem<SerialPipes>> getPipes() {
		return new SetCombiner<SerialPipes, Pipe>() {

			@Override
			public SetItem<SerialPipes> combine(List<Pipe> pipes, List<String> labels) {

				return new SetItem<SerialPipes>(new SerialPipes(pipes),
						labels.toString());
			}

		}.add(new SetFactory<Pipe>() {
			@Override
			protected void build() {
				this.add(new Input2CharSequence("UTF-8"), "Input2CharSequence");
				this.add(new UnescapeHTML(), "UnescapeHTML");
				this.add(new CharSequenceLowercase(), "CharSequenceLowercase");
			}
		}).add(new SetFactory<Pipe>(true) {
			@Override
			protected void build() {
				this.add(
						new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")),
						"numdig tokenizer");
				this.add(new CharSequence2TokenSequence(Pattern.compile("[^\\s]+")),
						"whitespace tokenizer");
				this.add(new Tokenizer(false, false, false, false, false, false, false,
						false, false, false, Case.lower), "simplistic twitter tokenizer");
				this.add(new Tokenizer(true, true, true, true, true, false, true, true,
						false, false, Case.lower),
						"twitter tokenizer with no RT--appleCase--no ngramming--and noPunct removed");
				this.add(new Tokenizer(true, true, true, true, true, true, true, true,
						false, false, Case.apple),
						"twitter tokenizer with everything but ngramming");
				this.add(new Tokenizer(false), "default twitter tokenizer");
			}

		}).add(new SetFactory<Pipe>() {
			@Override
			protected void build() {
				this.add(new TokenSequenceRemoveStopwords(),
						"TokenSequenceRemoveStopwords");
				this.add(new Stemmer(), "Stemmed");
			}

		}).add(new SetFactory<Pipe>(true) {
			@Override
			protected void build() {
				this.add(new TokenSequence2FeatureSequence(),
						"TokenSequence2FeatureSequence");
			}
		}).add(new SetFactory<Pipe>(true) {
			@Override
			protected void build() {
				this.add(new Target2Label(), "Target2Label");
			}

		}).add(new SetFactory<Pipe>(true) {
			@Override
			protected void build() {
				this.add(new FeatureSequence2FeatureVector(),
						"FeatureSequence2FeatureVector");
			}

		}).iterator();
	}

	public static SerialPipes getDefault() {
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		// pipeList.add(new PrintInput());
		pipe.add(new Input2CharSequence("UTF-8"));
		pipe.add(new UnescapeHTML());

		Pattern tokenPattern = Pattern.compile("[^\\s]+");
		// pipe.add(new CharSequence2TokenSequence(tokenPattern));
		pipe.add(new Tokenizer(false, false, false, false, false, false, false,
				false, false, false, Case.nochange));
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
