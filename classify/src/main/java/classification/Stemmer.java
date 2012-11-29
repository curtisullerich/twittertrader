package classification;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.tartarus.snowball.ext.PorterStemmer;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;
/**
 * Source: http://comments.gmane.org/gmane.comp.ai.mallet.devel/1724
 *
 */
public class Stemmer extends Pipe {

	private transient PorterStemmer stemmer;

    private static final long serialVersionUID = -2270190864067551888L;

    public Stemmer() {
            stemmer = new PorterStemmer();
    }

    private void readObject(ObjectInputStream aInputStream)
                    throws ClassNotFoundException, IOException {
            aInputStream.defaultReadObject();
            stemmer = new PorterStemmer();
    }

    public Instance pipe(Instance carrier) {
            TokenSequence tokenSequence = (TokenSequence) carrier.getData();
            TokenSequence stemmedTokenSequence = new TokenSequence();

            for (int i = 0; i < tokenSequence.size(); i++) {
                    Token token = tokenSequence.get(i);
                    stemmer.setCurrent(token.getText());
                    stemmer.stem();
                    String s = stemmer.getCurrent();
                    stemmedTokenSequence.add(new Token(s));
//                    if (!token.getText().equals(s)) {
//                    System.out.println(token.getText() + "   :   " + s);
//                    }
            }
            carrier.setData(stemmedTokenSequence);
            return carrier;
    }
}
