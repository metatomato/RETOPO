package gl.iglou.studio.retopo.MAPS;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
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
public class MapsFragment extends Fragment implements OnMapReadyCallback, MapsGUIFragment.OnMapsGUIListener {

    private final String TAG = "MapsManager";

    private MapsGUIFragment mMapsGUIFragment;
    private MapFragment mMapFragment;
    private GoogleMap mMap;

    private ArrayList<OnMapsEvent> mListeners;

    private TopoManagerFragment mTopoManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mListeners = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps_container, container, false);

        mMapsGUIFragment = (MapsGUIFragment) getChildFragmentManager()
                .findFragmentById(R.id.maps_gui);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapFragment = mMapsGUIFragment.getMapFragment();
        if(mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        } else {
            Log.v(TAG, "MapFragment is null!!");
        }

        mTopoManager = ((ReTopoActivity)getActivity()).getTopoManager();
    }


    public void setMapsListener(OnMapsEvent listener) {
        mListeners.add(listener);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
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

    public void printTrace(Trace trace) {

        mMapsGUIFragment.setMileStoneCard(
                trace.getDate(),
                trace.getTitle(),
                trace.getComment(),
                trace.getPhoto(0));
    }

    @Override
    public void onPinMeClick() {
        for(OnMapsEvent listener : mListeners) {
            listener.OnPinMeClick();
        }
        Topo topo = mTopoManager.getCurrentTopo();
        Location loc = topo.getLocation(0);
        addMarker(loc);
        updateCamera(loc);
        printTrace(topo);
    }
}
