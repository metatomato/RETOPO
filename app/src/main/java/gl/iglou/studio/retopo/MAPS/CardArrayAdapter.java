package gl.iglou.studio.retopo.MAPS;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import gl.iglou.studio.retopo.DATA.Topo;
import gl.iglou.studio.retopo.DATA.Trace;
import gl.iglou.studio.retopo.R;

/**
 * Created by metatomato on 02.03.15.
 */
public class CardArrayAdapter extends ArrayAdapter<Trace> {

    private final String TAG = "TimeLine";
    private Context mContext;
    private Trace[] mMilestones;

    public CardArrayAdapter(Context context, Trace[] traces) {
        super(context, R.layout.milestone_cellule, traces);

        mContext = context;
        mMilestones = traces;

    }

    @Override
    public int getCount() {
        return mMilestones.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MilestoneCard currentCard;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.milestone_cellule, parent, false);

            //convertView.setTag(currentCard);

        }

        currentCard = (MilestoneCard) convertView.findViewById(R.id.milestone_container);
        currentCard.setMileStoneCard(mMilestones[position]);
        /*
        else {
            currentCard = (MilestoneCard) convertView.getTag();
        }
*/


//        Log.v(TAG,"Position: " + String.valueOf(position) + " for " + mMilestones[position].getTitle());

        return convertView;
    }
}
