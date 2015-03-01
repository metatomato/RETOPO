package gl.iglou.studio.retopo.MAPS;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import gl.iglou.studio.retopo.ReTopoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsGUIFragment extends Fragment implements View.OnTouchListener, Animator.AnimatorListener{

    private final String TAG = "MapsGUI";

    private Button mButtonPin;
    private MapFragment mMapFragment;

    private View mTraceContainer;
    private ImageView mImageTrace;
    private TextView mTextTitle;
    private TextView mTextComment;
    private TextView mTextTime;
    private TextView mtextDate;

    private MilestoneCard mCard;

    private float mLastX = 0.f;
    private float initX = 0.f;
    private float dX = 0.f;
    private boolean mIsCardInitialed = false;
    private float mCardX,mCardY;
    private int mDisplayX, mDisplayY;
    private ObjectAnimator mAnimCardOut;
    private ObjectAnimator mAnimCardIn;


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
                animateCardIn();
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

        mCard = (MilestoneCard) rootView.findViewById(R.id.milestone_container_0);

        mTraceContainer.setOnTouchListener(this);

       mDisplayX = ((ReTopoActivity) getActivity()).getDisplayWidth();
       mDisplayY = ((ReTopoActivity)getActivity()).getDisplayHeigh();


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

        if(!mIsCardInitialed) {
            mCardX = mTraceContainer.getX();
            mCardY = mTraceContainer.getY();

            Log.v(TAG,"cardX: " + String.valueOf(mCardX)
                    + "cardY: " + String.valueOf(mCardY) );
            mIsCardInitialed = true;
        }

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initX = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                dX = initX - event.getRawX();
                Log.v(TAG,"dx " + String.valueOf(dX) + "    X: " + String.valueOf(mTraceContainer.getX()));
                mTraceContainer.setTranslationX(-dX);
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "ACTION_UP");
                float currentX = mTraceContainer.getX();
                float finalX = mCardX;
                long duration = 500;
                if(currentX < (-1* mTraceContainer.getWidth()/2f) ) {
                    finalX = -1* mTraceContainer.getWidth();
                    duration = 100;
                }
                mAnimCardOut = ObjectAnimator.ofFloat(mTraceContainer, "X", currentX, finalX);
                mAnimCardOut.setDuration(duration);
                mAnimCardOut.addListener(this);
                mAnimCardOut.start();
                break;
            case MotionEvent.ACTION_CANCEL:

        }

        return false;
    }

    private void animateCardIn() {
        mTraceContainer.setX(mCardX);
        mAnimCardIn = ObjectAnimator.ofFloat(mTraceContainer, "Y", mDisplayY, mCardY);
        mAnimCardIn.setDuration(400);
        mAnimCardIn.start();
    }


    public void setMileStoneCard(Long date, String title, String comment, String photo) {
        setTime(date);
        setDate(date);
        setTitle(title);
        setTextComment(comment);
        setImageTrace(photo);

        mCard.setMileStoneCard(date,title,comment,photo);
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

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(animation.equals(mAnimCardOut)) {
            if(mTraceContainer.getX() < mCardX) {
                animateCardIn();
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
