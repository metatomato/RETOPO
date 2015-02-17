package gl.iglou.studio.retopo;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;

import gl.iglou.studio.retopo.MAPS.MapsFragment;


public class ReTopoActivity extends ActionBarActivity {

    private DisplayMetrics mDisplayMetrics;
    private float mDpHeight;
    private float mDpWidth;

    final private int MAPS_FRAGMENT = 0;

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;
    private View mContentView;
    private View mNavigationDrawer;

    private MapsFragment mMapsGUIFragment;


    String[] mFragmentLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retopo);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mFragmentLabel = new String[4];
        mFragmentLabel[0] = getResources().getString(R.string.fragment_label_maps);

        //Layout Views Setup
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContentView = findViewById(R.id.main_content);
        mNavigationDrawer = findViewById(R.id.navigation_drawer);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        mDisplayMetrics = this.getResources().getDisplayMetrics();

        mDpHeight = mDisplayMetrics.heightPixels / mDisplayMetrics.density;
        mDpWidth = mDisplayMetrics.widthPixels / mDisplayMetrics.density;

        if (mToolbar  != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                return;
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                return;
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView,slideOffset);
                float moveFactor = (mNavigationDrawer.getWidth() * slideOffset);

                mContentView.setTranslationX(moveFactor);
            }
        };

        if(mDpWidth >= 600.f) {
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.iglou_logo);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //Start Maps Fragment
        FragmentManager fm = getFragmentManager();
        if(mMapsGUIFragment == null) {
            mMapsGUIFragment = new MapsFragment();
            fm.beginTransaction().add(mMapsGUIFragment, "BTFrag").commit();
        }

        contentViewResolver(MAPS_FRAGMENT);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mDpWidth >= 600.0) {
            //Disable the drawer shadow (shadow already attached to contentView)
            mNavigationDrawer.setBackgroundResource(0);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mContentView.setBackgroundDrawable(getResources().getDrawable(R.drawable.drawer_shadow_reverse_left));
                mContentView.setPadding(Math.round(5 * mDisplayMetrics.density),0,0,0);
            }
        }
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

        getSupportActionBar().setTitle(mFragmentLabel[fragmentId]);

        closeDrawer();
    }

    private void closeDrawer() {
        Handler scheduler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawers();
            }
        };
        scheduler.postDelayed( task, 150L);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    public float getDpWidth() {return mDpWidth;}
}
