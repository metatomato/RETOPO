package gl.iglou.studio.retopo.CLOUD;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gl.iglou.studio.retopo.R;


public class CloudManagerFragment extends Fragment {

    private final String TAG = "CLOUD";

    private CloudHelper mCloudHelper;

    public CloudManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v(TAG,"CloudManager onCreate call...");

        mCloudHelper = new CloudHelper();
        mCloudHelper.fetchDataFromUrl("http://retopo.studio.iglou.gl/topo");

    }

    @Override
    public void onStart() {

        Log.v(TAG, "CloudManager START!");
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
