package gl.iglou.studio.retopo.MAPS;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;

import gl.iglou.studio.retopo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsGUIFragment extends Fragment {

    private Button mButtonPin;
    private MapFragment mMapFragment;

    private OnMapsGUIListener mMapsManager;

    public MapsGUIFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        mButtonPin = (Button) rootView.findViewById(R.id.btn_pin);

        mButtonPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapsManager.onPinMeClick();
            }
        });

        mMapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mMapsManager = (OnMapsGUIListener) getParentFragment();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public MapFragment getMapFragment() {
        return mMapFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.re_topo,menu);
    }

    public interface OnMapsGUIListener {
        public void onPinMeClick();
    }
}
