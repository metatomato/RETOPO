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
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.google.android.gms.maps.MapFragment;

import gl.iglou.studio.retopo.DATA.Trace;
import gl.iglou.studio.retopo.R;
import gl.iglou.studio.retopo.ReTopoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsGUIFragment extends Fragment implements View.OnTouchListener, Animator.AnimatorListener{

    private final String TAG = "MapsGUI";

    public final int CURRENT_CARD = 0;
    public final int NEXT_CARD = 1;
    public final int PREV_CARD = 2;

    private ImageButton mButtonPin;
    private MapFragment mMapFragment;
    private MilestoneCard mCardTwo;
    private MilestoneCard mCardOne;
    private MilestoneCard mCardZero;
    private MilestoneCard mCurrentCard;
    private MilestoneCard mNextCard;
    private MilestoneCard mPrevCard;
    private MilestoneCard mMoveInCard;

    private float mDisplacementRatio = 0.f;
    private float initX = 0.f;
    private float initCardX = 0.f;
    private float initCardY = 0.f;
    private float dX = 0.f;
    private boolean mIsCardInitialed = false;
    private boolean mIsCardOut = false;
    private boolean mIsCardAnimated = false;
    private float mCardInX, mCardInY;
    private float mCardOffX, mCardOffY;
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
        mButtonPin = (ImageButton) rootView.findViewById(R.id.btn_pin);

        mButtonPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapsManager.onCardListClick();
            }
        });

        mMapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if(mMapFragment == null) {
            Log.v(TAG,"Map fragement is null in MapsGUI!!");
        } else {
            Log.v(TAG,"Catch a nice MapFragment in MapsGUI!!");
        }

        mMapsManager = (OnMapsGUIListener) getParentFragment();

        mCardZero = (MilestoneCard) rootView.findViewById(R.id.milestone_container);
        mCardOne = (MilestoneCard) rootView.findViewById(R.id.milestone_container_0);
        mCardTwo = (MilestoneCard) rootView.findViewById(R.id.milestone_container_1);

        mCardZero.setOnTouchListener(this);
        mCardOne.setOnTouchListener(this);
        mCardTwo.setOnTouchListener(this);

       mDisplayX = ((ReTopoActivity) getActivity()).getDisplayWidth();
       mDisplayY = ((ReTopoActivity)getActivity()).getDisplayHeigh();

        mCurrentCard = mCardZero;
        mNextCard = mCardOne;
        mPrevCard = mCardTwo;

        mMoveInCard = mNextCard;

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapsManager.onPostInit();
        Log.v(TAG,"MAPSGUI POST INIT!!");
    }

    public MapFragment getMapFragment() {
        return mMapFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.re_topo,menu);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(!mIsCardInitialed) {
            mCardInX = mCardZero.getX();
            mCardInY = mCardZero.getY();
            mCardOffX = mCardOne.getX();
            mCardOffY = mCardOne.getY();
            mCardWidth = mCardOne.getWidth();
            mCardHeight = mCardOne.getHeight();
            mIsCardInitialed = true;
        }

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initX = event.getRawX();
                initCardX = mCurrentCard.getX();
                initCardY = mNextCard.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dX = initX - event.getRawX();
                mDisplacementRatio = Math.abs(dX) / mCardWidth;
                if(!mIsCardAnimated) {
                    float currentY = initCardY - mCardHeight * mDisplacementRatio;
                    if(mCurrentCard.getX() < mCardInX) {
                        mNextCard.setY(currentY);
                        mNextCard.setAlpha(mDisplacementRatio);
                        mMoveInCard = mNextCard;

                    } else {
                        mPrevCard.setY(currentY);
                        mPrevCard.setAlpha(mDisplacementRatio);
                        mMoveInCard = mPrevCard;
                    }
                    mCurrentCard.setTranslationX(-dX);
                }
                break;
            case MotionEvent.ACTION_UP:
                float currentX = mCurrentCard.getX();
                float currentY = mMoveInCard.getY();
                float finalX = mCardInX;
                float finalY = mCardOffY;
                float finalAlpha = 0f;
                long duration = 500;
                if(Math.abs(currentX + mCardInX) > (mCardWidth/2f) ) {
                    finalX = currentX / Math.abs(currentX) * mCardWidth;
                    finalY = mCardInY;
                    finalAlpha = 1f;
                    duration = 100;
                    mIsCardOut = true;
                }
                animateCardOut(currentX,finalX,duration);
                animateCardIn(mMoveInCard,currentY,finalY,duration);
                animateCardFadeIn(mMoveInCard,mDisplacementRatio,finalAlpha,duration);
                break;
            case MotionEvent.ACTION_CANCEL:

        }

        return false;
    }

    private void animateCardOut(float from, float to, long duration) {
        mIsCardAnimated = true;
        mAnimCardOut = ObjectAnimator.ofFloat(mCurrentCard, "X", from, to);
        mAnimCardOut.setDuration(duration);
        mAnimCardOut.addListener(this);
        mAnimCardOut.start();
    }

    private void animateCardIn(MilestoneCard card, float from, float to, long duration) {
        mAnimCardIn = ObjectAnimator.ofFloat(card, "Y", from, to);
        mAnimCardIn.setDuration(400);
        mAnimCardIn.addListener(this);
        mAnimCardIn.setInterpolator(new OvershootInterpolator(5f));
        mAnimCardIn.start();
    }

    private void animateCardFadeIn(MilestoneCard card, float from, float to, long duration) {
        mAnimFadeCardIn = ObjectAnimator.ofFloat(card,"alpha", from,1f);
        mAnimFadeCardIn.setDuration(duration);
        mAnimFadeCardIn.start();
    }


    public void updateCard() {
        setCard(CURRENT_CARD,mMapsManager.getCurrent());
        setCard(NEXT_CARD,mMapsManager.getNext());
        setCard(PREV_CARD,mMapsManager.getPrev());
    }

    private void setCard(int cardId, Trace milestone) {
        switch(cardId) {
            case CURRENT_CARD:
                mCurrentCard.setMileStoneCard(milestone);
                break;
            case NEXT_CARD:
                mNextCard.setMileStoneCard(milestone);
                break;
            case PREV_CARD:
                mPrevCard.setMileStoneCard(milestone);
                break;
        }
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
            int cardType;
            mCurrentCard.setX(mCardOffX);
            mCurrentCard.setY(mCardOffY);
            MilestoneCard temp = mCurrentCard;
            mCurrentCard = mMoveInCard;

            if(mMoveInCard.equals(mNextCard)) {
                mNextCard = temp;
                mNextCard.setElevation(0);
                cardType = NEXT_CARD;
            } else {
                mPrevCard = temp;
                mPrevCard.setElevation(0);
                cardType = PREV_CARD;
            }

            mCurrentCard.setElevation(4 * ((ReTopoActivity) getActivity()).getDpWidth());

            mMapsManager.onCardChange(cardType);

            mIsCardOut = false;
        }

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }







    public interface OnMapsGUIListener {
        public void onCardListClick();
        public void onPostInit();
        public void onCardChange(int cardType);
        public Trace getNext();
        public Trace getCurrent();
        public Trace getPrev();

    }



}
