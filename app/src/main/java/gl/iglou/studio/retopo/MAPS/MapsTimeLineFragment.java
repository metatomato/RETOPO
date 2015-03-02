package gl.iglou.studio.retopo.MAPS;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import gl.iglou.studio.retopo.DATA.Topo;
import gl.iglou.studio.retopo.DATA.Trace;
import gl.iglou.studio.retopo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsTimeLineFragment extends Fragment {

    private final String TAG = "TimeLine";

    private MapsFragment mMapsManager;

    private Topo mCurrentTopo;

    private ListView mCardList;
    private CardArrayAdapter mCardArrayAdapter;


    public MapsTimeLineFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps_timeline,container,false);

        mMapsManager = (MapsFragment) getParentFragment();
        mCurrentTopo = mMapsManager.getCurrentTopo();


        mCardList = (ListView) rootView.findViewById(R.id.listView_cardlist);

        mCardArrayAdapter = new CardArrayAdapter(getActivity(), mCurrentTopo.getMilestonesSortedByTimestamp());

        mCardList.setAdapter(mCardArrayAdapter);

        mCardList.setOnItemClickListener(new CardListListener());

        return rootView;
    }

    class CardListListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            Log.v(TAG, "Touch on " + mCurrentTopo.getMilestone(position).getTitle() + " at " + String.valueOf(position));
            mMapsManager.onMilestoneSelected(position);
        }
    }

    public interface OnTimeLineListerner {
        public void onMilestoneSelected(int index);
    }
}
