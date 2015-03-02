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
    private ArrayList<Trace> mMilestones;
    private Trace[] mSortedMilestone;

    public Topo() {
        mTraces = new ArrayList<>();
        mMilestones = new ArrayList<>();
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
            mMilestones = extractMilestones();
            mSortedMilestone = getMilestonesSortedByTimestamp();
        }

        Log.v(TAG, "Topo details :\n" + toString());
    }

    private ArrayList<Trace> extractMilestones() {

        for(Trace trace : mTraces) {
            ArrayList<String> tags = trace.getTags();
            boolean isMilestone = false;
            for(String tag : tags) {
                if( !tag.contentEquals("topo") && !tag.contentEquals("GPX") ) {
                    isMilestone = true;
                }
            }
            if(isMilestone) {
                mMilestones.add(trace);
            }
        }

        Log.v(TAG,"Extracted milestone: ");
        for(Trace trace : mMilestones) {
            Log.v(TAG,trace.toString());
        }

        return mMilestones;
    }

    public ArrayList<Trace> getMilestones() {
        return mMilestones;
    }

    public int getMilestoneNum() {
        return mMilestones.size();
    }

    public Trace getMilestone(int index) {
        Trace milestone = new Trace();
        if(index < mSortedMilestone.length) {
            milestone = mSortedMilestone[index];
        }
        return milestone;
    }

    public int indexOf(Trace milestone) {
        int index = -1;
        int i = 0;
        for(Trace trace : mSortedMilestone){
            if(trace.getDate() == milestone.getDate()) {
                index = i;
            }
            i++;
        }
        return index;
    }

    public Trace[] getMilestonesSortedByTimestamp() {
        ArrayList<Trace> milestones = new ArrayList<>();

        for(Trace milestone : mMilestones) {
            Log.v(TAG,"mMilestones: " + milestone.getTitle());
            if(milestones.isEmpty()) {
                milestones.add(milestone);
            } else {
                int idx = milestones.size();
                for (Trace sortedMilestone : milestones) {
                    if (milestone.getDate() < sortedMilestone.getDate()) {
                        idx = milestones.indexOf(sortedMilestone);
                    }
                }
                milestones.add(idx, milestone);
            }
        }

        Trace[] sortedMilestones = milestones.toArray(new Trace[milestones.size()]);

        for(Trace trace : sortedMilestones) {
            Log.v(TAG,"sorted : " + trace.getTitle());
        }
        return sortedMilestones;
    }

    public String[] getTopoPhotos() {
        ArrayList<String> photos = new ArrayList<>();

        for(Trace trace : mTraces) {
            for(String photo : trace.getPhotos()) {
                if(photo != null && !photo.isEmpty()) {
                    if(!photos.contains(photo)) {
                        photos.add(photo);
                        Log.v(TAG, "photo loaded " + photo);
                    }
                }
            }
        }
        return photos.toArray(new String[photos.size()]);
    }

    public Location getLocation() {
        return getLocation(0);
    }
}



































