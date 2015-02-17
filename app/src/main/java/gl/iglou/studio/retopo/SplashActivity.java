package gl.iglou.studio.retopo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends Activity {
    private static final String TAG = "SPALSH_ACTIVITY";

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }


    @Override
    protected void onStart() {
        super.onStart();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NextActivity();
            }
        }, SPLASH_TIME_OUT);
    }

    public void NextActivity() {
        Intent i = new Intent(SplashActivity.this, ReTopoActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }
}
