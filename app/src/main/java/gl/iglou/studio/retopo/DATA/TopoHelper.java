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

    static public Trace extractTrace(JSONObject object) {
        Trace trace = new Trace();
        try {
            JSONObject jsonGeometry = object.getJSONObject("geometry");
            JSONObject jsonProperties = object.getJSONObject("properties");
            JSONArray jsonCoord = jsonGeometry.getJSONArray("coordinates");
            JSONArray jsonPhotos = jsonProperties.getJSONArray("photos");
            JSONArray jsonTags = jsonProperties.getJSONArray("tags");

            Long date = 0L;
            String title = "";
            String comment = "";
            String provider = "JsonImport";
            ArrayList<Location> GPS = new ArrayList<>();
            ArrayList<String> photos = new ArrayList<>();
            ArrayList<String> tags = new ArrayList<>();
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


            date = jsonProperties.getLong("date");
            title = jsonProperties.getString("title");
            comment = jsonProperties.getString("comment");


            int photoNum = jsonPhotos.length();
            for(int i = 0 ; i < photoNum ; i++  )
            {
                photos.add(jsonPhotos.getString(i));
            }

            int tagNum = jsonTags.length();
            for(int i = 0 ; i < tagNum ; i++  )
            {
                tags.add(jsonTags.getString(i));
            }

            trace.setDate(date);
            trace.setTitle(title);
            trace.setComment(comment);
            trace.setLocations(GPS);
            trace.setPhotos(photos);
            trace.setTags(tags);

        }
        catch(JSONException e) {
            Log.v(TAG,"Failed to extract the GPS data from JSON Array");
        }

        //Log.v(TAG,"Extracted : " + trace.toString());

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
