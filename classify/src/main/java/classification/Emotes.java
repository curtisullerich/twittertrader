package classification;

import java.util.regex.Pattern;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;
/**
 * Source: http://comments.gmane.org/gmane.comp.ai.mallet.devel/1724
 *
 */
public class Emotes extends Pipe {

    private static final long serialVersionUID = -22701908667551888L;
    
    private Pattern smile = Pattern.compile("#(^|\\W)(\\>\\:\\]|\\:-\\)|\\:\\)|\\:o\\)|\\:\\]|\\:3|\\:c\\)|\\:\\>|\\=\\]|8\\)|\\=\\)|\\:\\}|\\:\\^\\))($|\\W)#");
    private Pattern laugh = Pattern.compile("#(^|\\W)(\\>\\:D|\\:-D|\\:D|8-D|x-D|X-D|\\=-D|\\=D|\\=-3|8-\\)|xD|XD|8D|\\=3)($|\\W)#");
    private Pattern sad = Pattern.compile("#(^|\\W)(\\>\\:\\[|\\:-\\(|\\:\\(|\\:-c|\\:c|\\:-\\<|\\:-\\[|\\:\\[|\\:\\{|\\>\\.\\>|\\<\\.\\<|\\>\\.\\<)($|\\W)#");
    private Pattern wink = Pattern.compile("#(^|\\W)(\\>;\\]|;-\\)|;\\)|\\*-\\)|\\*\\)|;-\\]|;\\]|;D|;\\^\\))($|\\W)#");
    private Pattern tongue = Pattern.compile("#(^|\\W)(\\>\\:P|\\:-P|\\:P|X-P|x-p|\\:-p|\\:p|\\=p|\\:-Þ|\\:Þ|\\:-b|\\:b|\\=p|\\=P|xp|XP|xP|Xp)($|\\W)#");
    private Pattern surprise = Pattern.compile("#(^|\\W)(\\>\\:o|\\>\\:O|\\:-O|\\:O|°o°|°O°|\\:O|o_O|o\\.O|8-0)($|\\W)#");
    private Pattern annoyed = Pattern.compile("#(^|\\W)(\\>\\:\\\\|\\>\\:/|\\:-/|\\:-\\.|\\:\\\\|\\=/|\\=\\\\|\\:S|\\:\\/)($|\\W)#");
    private Pattern cry = Pattern.compile("#(^|\\W)(\\:'\\(|;'\\()($|\\W)#");

    public Emotes() {
    }

    public Instance pipe(Instance carrier) {
    	String data = (String)carrier.getData();
    	
    	if(data == null)
    		return carrier;
    	
//    	data = smile.matcher(data).replaceAll("hasSmileEmoticon");
//    	data = laugh.matcher(data).replaceAll("hasLaughEmoticon");
//    	data = sad.matcher(data).replaceAll("hasSadEmoticon");
//    	data = wink.matcher(data).replaceAll("hasWinkEmoticon");
//    	data = tongue.matcher(data).replaceAll("hasTongueEmoticon");
//    	data = surprise.matcher(data).replaceAll("hasSurpiseEmoticon");
//    	data = annoyed.matcher(data).replaceAll("hasAnnoyedEmoticon");
//    	data = cry.matcher(data).replaceAll("hasCryEmoticon");
    	
    	data = smile.matcher(data).replaceAll("emoticon");
    	data = laugh.matcher(data).replaceAll("emoticon");
    	data = sad.matcher(data).replaceAll("emoticon");
    	data = wink.matcher(data).replaceAll("emoticon");
    	data = tongue.matcher(data).replaceAll("emoticon");
    	data = surprise.matcher(data).replaceAll("emoticon");
    	data = annoyed.matcher(data).replaceAll("emoticon");
    	data = cry.matcher(data).replaceAll("emoticon");
    	
    	carrier.setData(data);
    	
    	return carrier;
    }
}
