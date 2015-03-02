package gl.iglou.studio.retopo.MAPS;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;

import gl.iglou.studio.retopo.R;
import gl.iglou.studio.retopo.ReTopoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsGUIFragment extends Fragment implements View.OnTouchListener, Animator.AnimatorListener{

    private final String TAG = "MapsGUI";

    private Button mButtonPin;
    private MapFragment mMapFragment;

    private MilestoneCard mCardOne;
    private MilestoneCard mCardZero;
    private View mOnScreenCard;
    private View mOffScreenCard;

    private float mDisplacementRatio = 0.f;
    private float initX = 0.f;
    private float initCardX = 0.f;
    private float initCardY = 0.f;
    private float dX = 0.f;
    private boolean mIsCardInitialed = false;
    private boolean mIsCardOut = false;
    private boolean mIsCardAnimated = false;
    private float mCardZeroX, mCardZeroY;
    private float mCardOneX, mCardOneY;
    private float mCardWidth, mCardHeight;
    private int mDisplayX, mDisplayY;
    private ObjectAnimator mAnimCardOut;
    private ObjectAnimator mAnimCardIn;
    private ObjectAnimator mAnimFadeCardIn;


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
            }
        });

        mMapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mMapsManager = (OnMapsGUIListener) getParentFragment();

        mCardZero = (MilestoneCard) rootView.findViewById(R.id.milestone_container);

        mCardOne = (MilestoneCard) rootView.findViewById(R.id.milestone_container_0);

        mCardZero.setOnTouchListener(this);
        mCardOne.setOnTouchListener(this);

       mDisplayX = ((ReTopoActivity) getActivity()).getDisplayWidth();
       mDisplayY = ((ReTopoActivity)getActivity()).getDisplayHeigh();

        mOnScreenCard = mCardZero;
        mOffScreenCard = mCardOne;

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
            mCardZeroX = mCardZero.getX();
            mCardZeroY = mCardZero.getY();
            mCardOneX = mCardOne.getX();
            mCardOneY = mCardOne.getY();
            mCardWidth = mCardOne.getWidth();
            mCardHeight = mCardOne.getHeight();
            mIsCardInitialed = true;
        }

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initX = event.getRawX();
                initCardX = mOnScreenCard.getX();
                initCardY = mOffScreenCard.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dX = initX - event.getRawX();
                mDisplacementRatio = dX / mCardWidth;
                if(!mIsCardAnimated) {
                    mOnScreenCard.setTranslationX(-dX);
                    mOffScreenCard.setY(initCardY - mCardHeight * mDisplacementRatio);
                    mOffScreenCard.setAlpha(mDisplacementRatio);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "ACTION_UP");
                float currentX = mOnScreenCard.getX();
                float currentY = mOffScreenCard.getY();
                float finalX = mCardZeroX;
                float finalY = mCardOneY;
                float finalAlpha = 0f;
                long duration = 500;
                if(currentX < (-1* mCardWidth/2f) ) {
                    finalX = -1* mCardWidth;
                    finalY = mCardZeroY;
                    finalAlpha = 1f;
                    duration = 100;
                    mIsCardOut = true;
                }
                animateCardOut(currentX,finalX,duration);
                animateCardIn(currentY,finalY,duration);
                animateCardFadeIn(mDisplacementRatio,finalAlpha,duration);
                break;
            case MotionEvent.ACTION_CANCEL:

        }

        return false;
    }

    private void animateCardOut(float from, float to, long duration) {
        mIsCardAnimated = true;
        mAnimCardOut = ObjectAnimator.ofFloat(mOnScreenCard, "X", from, to);
        mAnimCardOut.setDuration(duration);
        mAnimCardOut.addListener(this);
        mAnimCardOut.start();
    }

    private void animateCardIn(float from, float to, long duration) {
        mAnimCardIn = ObjectAnimator.ofFloat(mOffScreenCard, "Y", from, to);
        mAnimCardIn.setDuration(400);
        mAnimCardIn.addListener(this);
        mAnimCardIn.setInterpolator(new OvershootInterpolator(5f));
        mAnimCardIn.start();
    }

    private void animateCardFadeIn(float from, float to, long duration) {
        mAnimFadeCardIn = ObjectAnimator.ofFloat(mOffScreenCard,"alpha", from,1f);
        mAnimFadeCardIn.setDuration(duration);
        mAnimFadeCardIn.start();
    }


    public void setMileStoneCard(Long date, String title, String comment, String photo) {

        mCardZero.setMileStoneCard(date, title, comment, photo);
        mCardOne.setMileStoneCard(date, title, comment, photo);
    }



    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(animation.equals(mAnimCardOut)) {

        }
        if(animation.equals(mAnimCardIn)){
            mIsCardAnimated = false;
        }
        if(mIsCardOut) {
            mOnScreenCard.setX(mCardOneX);
            mOnScreenCard.setY(mCardOneY);
            View temp = mOnScreenCard;
            mOnScreenCard = mOffScreenCard;
            mOffScreenCard = temp;
            mOnScreenCard.setElevation(4 * ((ReTopoActivity)getActivity()).getDpWidth());
            mOffScreenCard.setElevation(0);
            mIsCardOut = false;
        }

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
