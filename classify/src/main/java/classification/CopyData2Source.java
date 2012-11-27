package classification;

import cc.mallet.pipe.Pipe;
import cc.mallet.types.Instance;

/**
 * Source: http://code.google.com/p/dualist/source/browse/trunk/core/src/dualist/pipes/CopyData2Source.java?r=10
 *
 */
public class CopyData2Source extends Pipe {
    private static final long serialVersionUID = 1;

    public Instance pipe (Instance carrier) {
        carrier.setSource(carrier.getData());
        return carrier;
    }

}
