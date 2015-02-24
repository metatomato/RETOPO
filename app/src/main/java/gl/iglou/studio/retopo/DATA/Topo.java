package gl.iglou.studio.retopo.DATA;

import java.util.ArrayList;

/**
 * Created by metatomato on 24.02.15.
 */
public class Topo extends Trace {

    private ArrayList<Trace> mTraces;

    Topo(ArrayList<Trace> traces) {
        mTraces = traces;
    }

    public ArrayList<Trace> getTraces() {
        return mTraces;
    }
}
