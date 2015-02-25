package gl.iglou.studio.retopo.MAPS;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import gl.iglou.studio.retopo.R;
import gl.iglou.studio.retopo.ReTopoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private MapFragment mMapFragment;
    private GoogleMap mMap;

    private Button mButtonPin;

    private ArrayList<OnMapsEvent> mListeners;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mListeners = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mButtonPin = (Button) rootView.findViewById(R.id.btn_pin);

        mButtonPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePosition();
                for(OnMapsEvent listener : mListeners) {
                    listener.OnPinMeClick();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.re_topo,menu);
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
}
