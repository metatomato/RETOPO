package gl.iglou.studio.retopo.DATA;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by metatomato on 24.02.15.
 */
public class Topo extends Trace {

    private final String TAG = "Topo";

    private ArrayList<Trace> mTraces;

    public Topo() {
        mTraces = new ArrayList<>();
    };

    public Topo(ArrayList<Trace> traces) {
       setTraces(traces);
    }

    public ArrayList<Trace> getTraces() {
        return mTraces;
    }

    public void setTraces(ArrayList<Trace> traces) {
        mTraces.clear();
        mTraces = traces;

        if(!mTraces.isEmpty()) {
            init(mTraces.get(0));
        }

        Log.v(TAG, "Topo details :\n" + toString());
    }

    public Location getLocation() {
        return getLocation(0);
    }
}
