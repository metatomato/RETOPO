package gl.iglou.studio.retopo.MAPS;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import java.io.InputStream;
import java.util.Calendar;

import gl.iglou.studio.retopo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsGUIFragment extends Fragment implements View.OnTouchListener{

    private final String TAG = "MapsGUI";

    private Button mButtonPin;
    private MapFragment mMapFragment;

    private View mFrameTouch;
    private View mTraceContainer;
    private ImageView mImageTrace;
    private TextView mTextTitle;
    private TextView mTextComment;
    private TextView mTextTime;
    private TextView mtextDate;

    private float mLastX = 0.f;

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
                mTraceContainer.setX(0);
            }
        });

        mMapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mMapsManager = (OnMapsGUIListener) getParentFragment();

        mTraceContainer = rootView.findViewById(R.id.milestone_container);
        mImageTrace = (ImageView) rootView.findViewById(R.id.image_trace);
        mTextTitle = (TextView) rootView.findViewById(R.id.text_title);
        mTextComment = (TextView) rootView.findViewById(R.id.text_comment);
        mTextTime = (TextView) rootView.findViewById(R.id.text_time);
        mtextDate = (TextView) rootView.findViewById(R.id.text_date);

        mFrameTouch = rootView.findViewById(R.id.frame_touch);
        mFrameTouch.setOnTouchListener(this);

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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float currentX = event.getX();
        float dX =  currentX - mLastX;
        if(v.equals(mTraceContainer)) {
           mTraceContainer.setTranslationX(currentX); ;
        }
        Log.v(TAG,"X: " + String.valueOf(currentX));
        mLastX = currentX;

        return false;
    }



    public void setMileStoneCard(Long date, String title, String comment, String photo) {
        setTime(date);
        setDate(date);
        setTitle(title);
        setTextComment(comment);
        setImageTrace(photo);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setTextComment(String comment) {
        mTextComment.setText(comment);
    }

    public void setImageTrace(String image) {
        int imageId = this.getResources().getIdentifier(
                image.substring(0, image.lastIndexOf('.'))
                , "raw"
                , getActivity().getPackageName()
        );

        InputStream imageIS = this.getResources().openRawResource(imageId);
        Bitmap myImage = BitmapFactory.decodeStream(imageIS);

        mImageTrace.setImageBitmap(myImage);
    }

    public void setTime(Long date) {
        mTextTime.setText(android.text.format.DateFormat.getTimeFormat(getActivity()).format(date * 1000L));
    }

    public void setDate(Long date) {
        mtextDate.setText(android.text.format.DateFormat.getDateFormat(getActivity()).format(date * 1000L));
    }

}
