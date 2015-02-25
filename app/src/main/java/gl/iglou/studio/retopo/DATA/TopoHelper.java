package gl.iglou.studio.retopo.DATA;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by metatomato on 24.02.15.
 */
public class TopoHelper {

    static private String TAG = "DataHelper";

    static private String GPS_KEY = "GPS";

    static public Trace extractTrace(JSONObject object) {
        Trace trace = new Trace();
        String title = "";
        String provider = "JsonImport";
        ArrayList<Location> GPS = new ArrayList<>();
        try {
            JSONObject jsonGeometry = object.getJSONObject("geometry");
            JSONObject jsonProperties = object.getJSONObject("properties");
            JSONArray jsonCoord = jsonGeometry.getJSONArray("coordinates");

            double latitude = 0.0;
            double longitude = 0.0;
            int len = jsonCoord.length();

            if(len > 2 ) {
                for(int i = 0 ; i < len; i++) {
                    latitude = jsonCoord.getJSONArray(i).getDouble(0);
                    longitude = jsonCoord.getJSONArray(i).getDouble(1);
                    Location loc = new Location(provider);
                    loc.setLatitude(latitude);
                    loc.setLongitude(longitude);
                    GPS.add(loc);
                }
            } else {
                latitude = jsonCoord.getDouble(0);
                longitude = jsonCoord.getDouble(1);
                Location loc = new Location(provider);
                loc.setLatitude(latitude);
                loc.setLongitude(longitude);
                GPS.add(loc);
            }

            title = jsonProperties.getString("title");

            Log.v(TAG,"Trace extracted : " + title);
            for(Location loc : GPS) {
                Log.v(TAG, "lat: " + loc.getLatitude() + "    lon: " + loc.getLongitude());
            }

            trace.setLocations(GPS);
            trace.setTitle(title);

        }
        catch(JSONException e) {
            Log.v(TAG,"Failed to extract the GPS data from JSON Array");
        }
        return trace;
    }

    static public Topo extractTopo(JSONObject object) {
        Topo topo = new Topo();
        ArrayList<Trace> traces = new ArrayList<>();
        try {
            JSONArray jsonTopo = object.getJSONArray("topo");
            for(int i = 0 ; i < jsonTopo.length() ; i++) {
                Trace trace = extractTrace(jsonTopo.getJSONObject(i));
                traces.add(trace);
                Log.v(TAG,"Add trace " + trace.getTitle());
            }
            topo.setTraces(traces);
        }
        catch(JSONException e)
        {
            Log.v(TAG,"Failed to extract topo from JSONObject");
        }
        return topo;
    }

}
