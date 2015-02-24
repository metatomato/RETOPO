package gl.iglou.studio.retopo.DATA;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by metatomato on 24.02.15.
 */
public class Trace {

    private ArrayList<Location> mLocations;

    private String mTitle;

    private String mImage;

    public Trace() {
        mLocations = new ArrayList<>();
        mLocations.add(new Location("Trace class default"));
        mTitle = "";
        mImage = "";
    }

    public Trace(Location loc, String title) {
        mLocations = new ArrayList<>();
        mLocations.add(loc);
        mTitle = title;
        mImage = "";
    }

    public Trace(Location loc, String title, String image) {
        mLocations = new ArrayList<>();
        mLocations.add(loc);
        mTitle = title;
        mImage = image;
    }

    public void setLocations(ArrayList<Location> locations) {
        mLocations.clear();
        mLocations = locations;
    }

    public Location getLocation(int index) {
        if(index < mLocations.size()) {
            return mLocations.get(index);
        } else {
            return new Location("Trace class default");
        }
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return mImage;
    }
}
