package classification;

import java.util.Random;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

public class Apple extends Pipe {
	
	
	@Override
	public Instance pipe(Instance carrier) {
		Random gen = new Random(System.currentTimeMillis());
		
		StringBuilder apple = new StringBuilder();
		for (int i = 0; i < gen.nextInt(42); ++i) {
			apple.append("Apple ");
		}
		
		carrier.setData(apple.toString());
		return carrier;
	}
}
