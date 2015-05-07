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
public class TopoManagerFragment extends Fragment{

    private final String TAG = "TopoManager";

    private DataManagerFragment mDataManager;

    private ArrayList<Topo> mTopos;

    private ArrayList<File> mTopoFiles;

    private Topo mCurrentTopo;


    public TopoManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopos = new ArrayList<>();
        mTopoFiles = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDataManager = ((ReTopoActivity)getActivity()).getDataManager();

        ArrayList<File> files = mDataManager.getTopoFiles();
        for(File file : files){
            mTopos.add(mDataManager.loadTopoFromFile(file));
            mTopoFiles.add(file);
            Log.v(TAG,"Found Topo file " + file.getName());
        }

        if(!mTopos.isEmpty()) {
            mCurrentTopo = mTopos.get(0);
        }

       // mCurrentTopo = mDataManager.loadDummyTopo();

    }

    public Topo getCurrentTopo() {
        return mCurrentTopo;
    }


}
