package classification;

import org.xeustechnologies.googleapi.spelling.SpellChecker;
import org.xeustechnologies.googleapi.spelling.SpellCorrection;
import org.xeustechnologies.googleapi.spelling.SpellResponse;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

public class SpellCheck extends Pipe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5452951467629627217L;
	
	/**
	 * Takes the given instance and does a spell check on the tweet text, replacing misspelled words with the
	 * best correction. The spell check is taken from the Google SpellChecker api
	 */
	@Override
	public Instance pipe(Instance carrier) {
		String text = (String) carrier.getData();
		
		if (text == null) {
			return carrier;
		}
		
		//Sends the text to Google to be spell checked
		SpellResponse resp = new SpellChecker().check(text);
		
		StringBuilder sb = new StringBuilder();
		int lastIndex = 0;
		if (resp.getCorrections() != null) {
			for (SpellCorrection sc: resp.getCorrections()) {
				/*
				 * If we got a valid correction then add the first (most likely to be correct) 
				 * correction in place of the old word
				*/
				if (sc.getConfidence() != 0) {
					/*
					 * Get everything that hasn't been appended already that comes before the 
					 * newly spell checked word
					*/
					sb.append(text.substring(lastIndex, sc.getOffset()));
					//TODO do we need to check that this exists or will it always be there because 
					//of the confidence or does the spell checker always try to match words with something?
					sb.append(sc.getWords()[0]);
					lastIndex = sc.getOffset() + sc.getLength();
				}
			}
		}
		
		//Flush the buffer!
		sb.append(text.substring(lastIndex, text.length()));
		
		carrier.setData(sb.toString());
		return carrier;
	}

}
