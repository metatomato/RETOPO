package gl.iglou.studio.retopo.DATA;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import gl.iglou.studio.retopo.R;

import gl.iglou.studio.retopo.DATA.FileSystemHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataManagerFragment extends Fragment {

    private final String TAG = "DataManagerFragment";

    private final String ROOT_PATH = "RETOPO";
    private final String DATA_PATH = "data";

    private File mRootFile;
    private File mDataFile;

    private ArrayList<File> mTopoFiles;

    public DataManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       checkLocalDirTree();
       parseData();

    }

    private void checkLocalDirTree() {
        if( FileSystemHelper.isExternalStorageWritable() ) {
            mRootFile = FileSystemHelper.LoadOrCreatePath(ROOT_PATH);
            mDataFile = FileSystemHelper.LoadOrCreatePath(ROOT_PATH + "/" + DATA_PATH);
        }
        else {
            Log.v(TAG, " Device External Storage is not writable!");
        }
    }

    private void parseData() {
        mTopoFiles = FileSystemHelper.getFilesByExtension(mDataFile, ".topo");
        for(File file : mTopoFiles) {
            Log.v(TAG,file.getName() + " FOUND!");
            String data = DataHelper.FileToString(file);
            JSONObject object = DataHelper.stringToJsonObject(data);
            Log.v(TAG,"Got " + object.length() + " mapping!");
        }
    }
}
