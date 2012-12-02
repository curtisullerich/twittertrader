package pipe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellChecker;

public class SpellCheck extends Pipe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5452951467629627217L;
	
	private SpellDictionaryHashMap dictionary;
	private SpellChecker spellchecker;
	
	private String dictionaryLoc = "../classify/eng_com.dic";
	private int threshold = 100;
	
	//Look at this block!
	{
		try {
			dictionary = new SpellDictionaryHashMap(new File(dictionaryLoc));
			spellchecker = new SpellChecker(dictionary);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
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
		
		StringTokenizer st = new StringTokenizer(text);
		StringBuilder result = new StringBuilder();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			//One letter words don't play well  with others
			if (token.length() != 1) {
				List<Word> words = (List<Word>) spellchecker.getSuggestions(token, threshold);
				Word best = findBest(words);
				token = (best != null && best.getCost() < threshold) ? best.getWord() : token;
			}
			result.append(token).append(" ");
		}
		carrier.setData(result.toString());
		return carrier;
	}
	
	private Word findBest(List<Word> words) {
		if (words.size() == 0) {
			return null;
		}
		Word best = words.get(0);
		for (Word w: words) {
			if (w.getCost() < best.getCost()) {
				best = w;
			}
		}
		return best;
	}


}
