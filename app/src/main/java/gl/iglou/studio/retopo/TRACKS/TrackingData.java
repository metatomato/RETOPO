package gl.iglou.studio.retopo.TRACKS;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by metatomato on 11.05.15.
 */
public class TrackingData {

    private String mName;
    private ArrayList<Location> mLocations;

    public TrackingData() {
        mName = "";
        mLocations = new ArrayList<>();
    }
}
