package gl.iglou.studio.retopo.DATA;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by metatomato on 24.02.15.
 */
public class Topo extends Trace {

    private ArrayList<Trace> mTraces;

    public Topo() {
        mTraces = new ArrayList<>();
    };

    public Topo(ArrayList<Trace> traces) {
        mTraces = traces;
    }

    public ArrayList<Trace> getTraces() {
        return mTraces;
    }

    public void setTraces(ArrayList<Trace> traces) {
        mTraces.clear();
        mTraces = traces;

        setTitle(traces.get(0).getTitle());
        setLocations(traces.get(0).getLocations());
    }

    public Location getLocation() {
        return getLocation(0);
    }
}
