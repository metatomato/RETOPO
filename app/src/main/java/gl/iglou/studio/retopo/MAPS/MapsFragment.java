package gl.iglou.studio.retopo.MAPS;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private final int WAIT_FOR_GOOGLE = 1000;
    private Handler mGoogleMapsAccess;
    private Runnable mGoogleMapsAccessTask;
    private int mGoogleAccessAttemp = 0;

    private MapsTimeLineFragment mTimeLineFragment;
    private MapsGUIFragment mMapsGUIFragment;
    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private boolean mIsGoogleMapReady = false;

    private ArrayList<OnMapsEvent> mListeners;

    private TopoManagerFragment mTopoManager;
    private Topo mCurrentTopo;
    private Trace mCurrentMilestone;
    private int mCurrentMilestoneIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mListeners = new ArrayList<>();

        mTimeLineFragment = new MapsTimeLineFragment();
        mMapsGUIFragment = new MapsGUIFragment();

        mGoogleMapsAccess = new Handler();
        mGoogleMapsAccessTask = new Runnable() {
            @Override
            public void run() {
                onGoogleAccessAttemp();
                onGoogleAccessSucess();
            }
        };
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
        mCurrentMilestone = mCurrentTopo.getMilestone(0);
        mCurrentMilestoneIndex = 0;
    }

    private void launchMapsFragment() {
        getChildFragmentManager().beginTransaction().
                replace(R.id.maps_frag_container, mMapsGUIFragment).commit();

    }


    public void onPostInit() {
        Log.v(TAG,"POST INIT CALL!!");
        mMapFragment = mMapsGUIFragment.getMapFragment();
        if(mMapFragment != null) {
            mMapFragment.getMapAsync(this);
            if(mIsGoogleMapReady) {
                onGoogleAccessSucess();
            } else {
                mGoogleMapsAccess.post(mGoogleMapsAccessTask);
            }

        } else {
            Log.v(TAG, "MapFragment is null!!");
       }
    }


    private void onGoogleAccessAttemp() {
        mGoogleAccessAttemp++;
        if(mGoogleAccessAttemp > 5) {
            Log.v(TAG,"Exceeded attemp max number... Aborting.");
            mGoogleMapsAccess.removeCallbacksAndMessages(null);
        }else {
            Log.v(TAG,"Attemp maps access " + String.valueOf(mGoogleAccessAttemp));
            mGoogleMapsAccess.postDelayed(mGoogleMapsAccessTask, WAIT_FOR_GOOGLE);
        }
    }

    private void onGoogleAccessSucess() {
        if(mIsGoogleMapReady) {
            Log.v(TAG,"Sucess! Google is ready!");
            setMilestoneCard(mCurrentTopo.getMilestone(mCurrentMilestoneIndex));
            Location loc = getCurrentTopo().getMilestone(mCurrentMilestoneIndex).getLocation(0);
            addMarker(loc);
            updateCamera(loc);
            mGoogleMapsAccess.removeCallbacksAndMessages(null);
        }
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

    private void updateCard() {
        setMilestoneCard(mCurrentMilestone);
    }


    public Topo getCurrentTopo() {
        return mTopoManager.getCurrentTopo();
    }

    public Trace getNext() {
        int currentMilestone = mCurrentMilestoneIndex;
        if((currentMilestone + 1) < mCurrentTopo.getMilestoneNum()) {
            currentMilestone++;
        } else {
            currentMilestone = 0;
        }

        return mCurrentTopo.getMilestone(currentMilestone);
    }

    public Trace getCurrent() {
        return mCurrentTopo.getMilestone(mCurrentMilestoneIndex);
    }

    public Trace getPrev() {
        int currentMilestone = mCurrentMilestoneIndex;
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
        mCurrentMilestoneIndex = index;
        mCurrentMilestone = mCurrentTopo.getMilestone(mCurrentMilestoneIndex);
        launchMapsFragment();
    }
}
