package gl.iglou.studio.retopo.TOPO;


import android.content.Context;
import android.content.SharedPreferences;
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

    private final String SAVED_STATE_SOCKET = "gl.iGLou.studio.retopo.SAVED_STATE_SOCKET";
    private final String SAVED_LAST_MODE = "SAVED_LAST_MODE";
    private final String SAVED_LAST_TOPO = "SAVED_LAST_TOPO";

    private DataManagerFragment mDataManager;

    private ArrayList<Topo> mTopos;

    private ArrayList<File> mTopoFiles;

    private Topo mCurrentTopo;

    private TopoSavedState mState;

    public TopoManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopos = new ArrayList<>();
        mTopoFiles = new ArrayList<>();
        mState = new TopoSavedState();
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

        loadState();
        Log.v(TAG,"Loaded State: " + mState.toString());
       // mCurrentTopo = mDataManager.loadDummyTopo();
    }

    public void enterDisplayMode() {
        writeState(TopoSavedState.TOPO_DISPLAY_MODE,mCurrentTopo.getTitle());
        Log.v(TAG,"enterDisplayMode");
    }

    public Topo getCurrentTopo() {
        return mCurrentTopo;
    }

    private void loadState() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(SAVED_STATE_SOCKET, Context.MODE_PRIVATE);
        int lastMode = sharedPref.getInt(SAVED_LAST_MODE, -1);
        mState.setLastMode(lastMode);
        String lastTopo = sharedPref.getString(SAVED_LAST_TOPO, "");
        mState.setLastTopo(lastTopo);
    }

    private void writeState(int currentMode, String currentTopo) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(SAVED_STATE_SOCKET, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SAVED_LAST_MODE, currentMode);
        editor.putString(SAVED_LAST_TOPO, currentTopo);
        editor.commit();
    }

}
