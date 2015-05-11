package gl.iglou.studio.retopo.TRACKS;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gl.iglou.studio.retopo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrackingFragment extends Fragment {


    private TrackingSetupFragment mTrackingSetupFragment;

    public TrackingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public void SetupNewTrackingSession() {

    }

}
