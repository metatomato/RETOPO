package gl.iglou.studio.retopo;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by metatomato on 04.12.14.
 */
public class LauncherCompactFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LauncherCompactFragment";
    private ArrayList<View> mRockets;
    private View mActiveRocket;
    private static final int ROCKET_TINT_ACTIVE = R.color.color_tertiary;
    private static final int ROCKET_TINT_PASSIVE = R.color.black_alpha;
    private LauncherCompactCallbacks mCallbacks;

    public LauncherCompactFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_launcher_compact, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRockets = getRockets();

        if(!mRockets.isEmpty()) {
            //mActiveRocket = mRockets.get(0);
            setRocketActive(mRockets.get(0));
        }

        for(View rocket : mRockets) {
            rocket.setOnClickListener(this);

            ((ImageView)rocket).getDrawable().setLevel(1);
        }

        updateRockets(mActiveRocket);
    }


    @Override
    public void onClick(View v) {
        if(mRockets.contains(v)) {
            setRocketActive(v);
            updateRockets(v);
            int rocketId = mRockets.indexOf(v);
            ((ReTopoActivity) getActivity()).contentViewResolver(rocketId);
        }
    }


    private ArrayList<View> getRockets() {
        ArrayList<View> result = new ArrayList<View>();
        ViewGroup viewGroup = (ViewGroup) getView();

        int count = viewGroup.getChildCount();


        if(((ReTopoActivity)getActivity()).getDpWidth() < 600.f) {
            count = viewGroup.getChildCount() - 1;
        }


        for (int i = 0; i < count ; i++) {
            View child = viewGroup.getChildAt(i);
            result.add(child);
            Log.v(TAG,"Found Rocket " + String.valueOf(i));
        }
        return result;
    }




    private void updateRockets(View v) {
        setRocketActivateState(v, true);

        for(View rocket : mRockets) {
            if(rocket != mActiveRocket) {
                setRocketActivateState(rocket, false);
                rocket.setSelected(false);
            }
        }
    }


    private void setRocketActive(View v) {
        if(mRockets.contains(v)) {
            mActiveRocket = v;
            mActiveRocket.setSelected(true);
        }

    }


    private void setRocketActivateState(View v, boolean activated) {
        ImageView rocket = (ImageView) v;
        if(rocket != null) {
            if(activated) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    rocket.getDrawable().setTint(getResources().getColor(ROCKET_TINT_ACTIVE));
                    rocket.setBackground(getResources().getDrawable(R.drawable.round_rect));
                }
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    rocket.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_rect));
                }
                */
            } else {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rocket.getDrawable().setTint(getResources().getColor(ROCKET_TINT_PASSIVE));
                }
                rocket.setBackgroundColor(getResources().getColor(R.color.white_solid));
            }
        }
    }



    public static interface LauncherCompactCallbacks {
        void onRocketLaunch(int rocketId);
    }
}
