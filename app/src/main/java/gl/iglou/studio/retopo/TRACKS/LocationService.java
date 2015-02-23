package gl.iglou.studio.retopo.TRACKS;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import gl.iglou.studio.retopo.ReTopoActivity;

/**
 * Created by metatomato on 20.02.15.
 */
public class LocationService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    final private String TAG = "LocationService";

    private Location mLastLocation;
    private Location mLastGPSLocation;

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingError = false;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";

    private Activity mContext;

    public LocationService(Activity activity) {
        mContext = activity;
        //buildGoogleApiClient();
        initGPSLocationService();
    }

    void initGPSLocationService() {
        Log.v(TAG,"Init Local GPS Service");
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                mLastGPSLocation = location;
                Log.v(TAG,"Received GPS location " + String.valueOf(location.getLatitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }


    public void startService() {
        if (!mResolvingError) {  // more about this later
            Log.v(TAG,"Start Service!");
           // mGoogleApiClient.connect();
        }
    }

    public void stopService() {
        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v(TAG,"CONNECTED!");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v(TAG,"CONNECTION SUSPENDED!");
    }

    //Google Play ConnectionFailed management begin
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG,"CONNECTION FAILED!");
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(mContext, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            //showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }
/*
    // The rest of this code is all about building the error dialog

    // Creates a dialog for an error message
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment(this);
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(mContext.getFragmentManager(), "errordialog");
    }

    // Called from ErrorDialogFragment when the dialog is dismissed.
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    // A fragment to display an error dialog
    public static class ErrorDialogFragment extends DialogFragment {

        public ErrorDialogFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            mService.onDialogDismissed();
        }
    }
    //Google Play ConnectionFailed management end
*/

    protected synchronized void buildGoogleApiClient() {
        Log.v(TAG,"Fetching GoogleApiClientRef...");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public Location getLocation() {
        if (mLastLocation != null) {
            Log.v(TAG,"Google API Location");
            return mLastLocation;
        }
        else if(mLastGPSLocation != null) {
            Log.v(TAG,"Google API Failed! Try with local GPS");
            return mLastGPSLocation;
        }
        else {
            Log.v(TAG,"Google API and local GPS Failed ");
            return new Location("tokyo");
        }
    }
}
