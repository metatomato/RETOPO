package gl.iglou.studio.retopo.MAPS;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import gl.iglou.studio.retopo.DATA.Topo;
import gl.iglou.studio.retopo.DATA.Trace;
import gl.iglou.studio.retopo.R;
import gl.iglou.studio.retopo.ReTopoActivity;
import gl.iglou.studio.retopo.TOPO.TopoManagerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, MapsGUIFragment.OnMapsGUIListener,
MapsTimeLineFragment.OnTimeLineListerner{

    private final String TAG = "MapsManager";

    private final int MAPS_FRAG = 0;
    private final int TIMELINE_FRAG = 1;

    private MapsTimeLineFragment mTimeLineFragment;
    private MapsGUIFragment mMapsGUIFragment;
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private boolean mIsGoogleMapReady = false;

    private ArrayList<OnMapsEvent> mListeners;

    private TopoManagerFragment mTopoManager;
    private Topo mCurrentTopo;
    private int mCurrentMilestone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mListeners = new ArrayList<>();

        mTimeLineFragment = new MapsTimeLineFragment();
        mMapsGUIFragment = new MapsGUIFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps_container, container, false);

        //mMapsGUIFragment = (MapsGUIFragment) getChildFragmentManager()
        //        .findFragmentById(R.id.maps_gui);


        getChildFragmentManager().beginTransaction().replace(R.id.maps_frag_container
                ,mTimeLineFragment,"TimeLineFragment").commit();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTopoManager = ((ReTopoActivity)getActivity()).getTopoManager();
        mCurrentTopo = mTopoManager.getCurrentTopo();
    }

    private void launchMapsFragment() {
        getChildFragmentManager().beginTransaction().
                replace(R.id.maps_frag_container, mMapsGUIFragment).commit();

    }


    public void onPostInit() {
        mMapFragment = mMapsGUIFragment.getMapFragment();
        if(mMapFragment != null) {
            mMapFragment.getMapAsync(this);
            if(mIsGoogleMapReady) {
                setMilestoneCard(mCurrentTopo.getMilestone(mCurrentMilestone));
            } else {
                Handler googleMapsAccess = new Handler();
                googleMapsAccess.post(new Runnable() {
                    @Override
                    public void run() {
                        googleMapsAccess.postDelayed(this,WAIT_FOR_GOOGLE);
                    }
                });
            }

        } else {
            Log.v(TAG, "MapFragment is null!!");
        }
    }


    private void onGoogleMapsAccess() {

    }

    public void setMapsListener(OnMapsEvent listener) {
        mListeners.add(listener);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mIsGoogleMapReady = true;
    }

    public void addMarker(Location loc) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(loc.getLatitude(), loc.getLongitude())));
    }

    public void addMarker(Location loc, String title) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                .title(title));
    }

    public void updatePosition() {
        Location loc = ((ReTopoActivity) getActivity()).getLocation();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                .title("I'm Here!"));
    }

    public void updateCamera(Location loc) {
        LatLng pos = new LatLng(loc.getLatitude(),loc.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));

    }

    public void setMilestoneCard(Trace milestone) {

        mMapsGUIFragment.setMileStoneCard(milestone);

    }


    public Topo getCurrentTopo() {
        return mTopoManager.getCurrentTopo();
    }

    public Trace getNext() {
        int currentMilestone = mCurrentMilestone;
        if((currentMilestone + 1) < mCurrentTopo.getMilestoneNum()) {
            currentMilestone++;
        } else {
            currentMilestone = 0;
        }

        return mCurrentTopo.getMilestone(currentMilestone);
    }

    public Trace getCurrent() {
        return mCurrentTopo.getMilestone(mCurrentMilestone);
    }

    public Trace getPrev() {
        int currentMilestone = mCurrentMilestone;
        if((currentMilestone - 1) > 0) {
            currentMilestone--;
        } else {
            currentMilestone = mCurrentTopo.getMilestoneNum() - 1;
        }

        return mCurrentTopo.getMilestone(currentMilestone);
    }

    @Override
    public void onCardListClick() {
        for(OnMapsEvent listener : mListeners) {
            listener.OnPinMeClick();
        }

        getChildFragmentManager().beginTransaction().replace(R.id.maps_frag_container
                ,mTimeLineFragment,"TimeLineFragment").commit();
    }

    public void requestCardUpdate(int index) {
        Location loc = mCurrentTopo.getMilestone(index).getLocation(0);
        addMarker(loc);
        updateCamera(loc);
    }

    @Override
    public void onMilestoneSelected(int index) {
        mCurrentMilestone = index;
        launchMapsFragment();
    }
}
