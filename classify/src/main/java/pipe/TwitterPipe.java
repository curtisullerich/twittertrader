package pipe;

import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequenceReplace;
import cc.mallet.pipe.FeatureSequence2AugmentableFeatureVector;
import cc.mallet.pipe.Noop;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.types.Instance;

/**
 * Source:
 * http://code.google.com/p/dualist/source/browse/trunk/core/src/dualist/
 * pipes/TwitterPipe.java?r=10
 * 
 */
public class TwitterPipe extends Pipe {

	private boolean anonmymize = true;
	private boolean emoticons = true;

	private String corePattern = "([\\@\\#]?[\\p{L}\\p{Mn}][\\p{L}\\p{Mn}'_]+)|([!?]+)";
	private String emoticonPattern = "([:;=x][-o^]?[)(/\\\\dp])|([/\\\\)(dp][-o^]?[:;=x])";

	private Pipe myPipe = new SerialPipes(
			new Pipe[] {
					new CopyData2Source(),
					// new CharSequenceLowercase(),
					anonmymize ? new CharSequenceReplace(Pattern.compile(
							"http\\:\\/\\/.*\\b", Pattern.CASE_INSENSITIVE),
							"HTTPLINK") : new Noop(),
					anonmymize ? new CharSequenceReplace(Pattern.compile(
							"\\@[\\p{L}\\p{Mn}]+", Pattern.CASE_INSENSITIVE),
							"@USERLINK") : new Noop(),
					new CharSequenceReplace(Pattern.compile("\\'"), ""),
					new CharSequenceReplace(Pattern.compile("\\!\\!+"), "!!"),
					new CharSequenceReplace(Pattern.compile("\\?\\?+"), "??"),
					// new CharSequenceReplace(Pattern.compile("\\bno\\s+"),
					// "no_"),
					// new CharSequenceReplace(Pattern.compile("\\bnot\\s+"),
					// "not_"),
					// new CharSequenceReplace(Pattern.compile("\\bdoesnt\\s+"),
					// "doesnt_"),
					// new CharSequenceReplace(Pattern.compile("\\bdont\\s+"),
					// "dont_"),
					// new CharSequenceReplace(Pattern.compile("\\baint\\s+"),
					// "aint_"),
					// emoticons
					// ? new FixEmoticons(Pattern.compile(emoticonPattern,
					// Pattern.CASE_INSENSITIVE))
					// : new Noop(),
					// emoticons
					// ? new
					// CharSequence2TokenSequence(Pattern.compile(corePattern+"|"+emoticonPattern,
					// Pattern.CASE_INSENSITIVE))
					// : new
					// CharSequence2TokenSequence(Pattern.compile(corePattern,
					// Pattern.CASE_INSENSITIVE)),
					new TokenSequenceBiGrammer(),
					new TokenSequenceRemoveStopwords(),
					new TokenSequence2FeatureSequence(),
					new FeatureSequence2AugmentableFeatureVector(),
					new Labelize(),
			// new PrintInputAndTarget(),
			});

	public Instance pipe(Instance carrier) {
		carrier.setSource(carrier.getData());
		return myPipe.pipe(carrier);
	}

	public java.util.Iterator<Instance> newIteratorFrom(
			java.util.Iterator<Instance> carrier) {
		return myPipe.newIteratorFrom(carrier);
	}

}
