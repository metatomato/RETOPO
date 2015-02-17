package gl.iglou.studio.retopo;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import gl.iglou.studio.retopo.MAPS.MapsFragment;


public class ReTopoActivity extends Activity {

    private DisplayMetrics mDisplayMetrics;
    private float mDpHeight;
    private float mDpWidth;

    final private int MAPS_FRAGMENT = 0;

    private MapsFragment mMapsGUIFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retopo);

        mDisplayMetrics = this.getResources().getDisplayMetrics();

        mDpHeight = mDisplayMetrics.heightPixels / mDisplayMetrics.density;
        mDpWidth = mDisplayMetrics.widthPixels / mDisplayMetrics.density;
    }

    public void contentViewResolver(int fragmentId) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment main_content_fragment;
        switch(fragmentId) {
            default:
            case MAPS_FRAGMENT :

                if(mMapsGUIFragment == null) {
                    mMapsGUIFragment = new MapsFragment();
                }
                main_content_fragment = mMapsGUIFragment;

                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.main_content, main_content_fragment)
                .commit();

       // getSupportActionBar().setTitle(mFragmentLabel[fragmentId]);

      //  closeDrawer();
    }

    public float getDpWidth() {return mDpWidth;}
}
