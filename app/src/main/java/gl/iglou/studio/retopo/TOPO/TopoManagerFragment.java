package gl.iglou.studio.retopo.TOPO;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

import gl.iglou.studio.retopo.DATA.DataManagerFragment;
import gl.iglou.studio.retopo.DATA.Topo;
import gl.iglou.studio.retopo.DATA.TopoHelper;
import gl.iglou.studio.retopo.MAPS.MapsFragment;
import gl.iglou.studio.retopo.MAPS.OnMapsEvent;
import gl.iglou.studio.retopo.R;
import gl.iglou.studio.retopo.ReTopoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopoManagerFragment extends Fragment implements OnMapsEvent{

    private final String TAG = "TopoManager";

    private MapsFragment mMaps;
    private DataManagerFragment mDataManager;

    private ArrayList<Topo> mTopos;


    public TopoManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopos = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMaps = ((ReTopoActivity)getActivity()).getMapsManager();
        mDataManager = ((ReTopoActivity)getActivity()).getDataManager();

        mMaps.setMapsListener(this);

        ArrayList<File> files = mDataManager.getTopoFiles();
        for(File file : files){
            mTopos.add(mDataManager.loadTopoFromFile(file));
        }

    }

    @Override
    public void OnPinMeClick() {

        Location loc = mTopos.get(0).getLocation();
        String title =  mTopos.get(0).getTitle();

        Log.v(TAG,"OnPinClick from TopoManager!");

        mMaps.addMarker(loc, title);
        mMaps.updateCamera(loc);


    }
}
