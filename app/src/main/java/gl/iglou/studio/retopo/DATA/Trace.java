package gl.iglou.studio.retopo.DATA;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by metatomato on 24.02.15.
 */
public class Trace {

    private ArrayList<Location> mLocations;

    private Long mDate;
    private String mTitle;
    private String mComment;
    private ArrayList<String> mPhotos;
    private ArrayList<String> mTags;

    public Trace() {
        mDate = 0L;
        mTitle = "";
        mComment = "";
        mLocations = new ArrayList<>();
        mPhotos = new ArrayList<>();
        mTags = new ArrayList<>();
    }

    public void init(Trace trace) {
        mDate = trace.mDate;
        mTitle = trace.mTitle;
        mComment = trace.mComment;
        mLocations = new ArrayList<>(trace.mLocations);
        mPhotos = new ArrayList<>(trace.mPhotos);
        mTags = new ArrayList<>(trace.mTags);
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

    protected ArrayList<Location> getLocations() {
        return mLocations;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public Long getDate() {
        return mDate;
    }

    public void setDate(Long date) {
        mDate = date;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public ArrayList<String> getPhotos() {
        return mPhotos;
    }

    public String getPhoto(int index) {
        String photo = "";
        if(index < mPhotos.size()) {
            photo = mPhotos.get(index);
        }
        return photo;
    }

    public void setPhotos(ArrayList<String> photos) {
        mPhotos = photos;
    }

    public ArrayList<String> getTags() {
        return mTags;
    }

    public void setTags(ArrayList<String> tags) {
        mTags = tags;
    }

    public String toString() {
        String description = "Trace Object \n";
        description += String.valueOf(mDate) + "\n";
        description += mTitle + "\n";
        description += mComment + "\n";
        for(Location loc : mLocations) {
            description += "[lat: " + String.valueOf(loc.getLatitude())
                    + "    lon: " + String.valueOf(loc.getLongitude()) + "]\n";
        }
        for(String s : mPhotos) {
            description += s + "\n";
        }
        for(String tag : mTags) {
            description += tag + "\n";
        }

        return description;
    }
}
