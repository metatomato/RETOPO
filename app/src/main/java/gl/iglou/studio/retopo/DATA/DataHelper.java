package gl.iglou.studio.retopo.DATA;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by metatomato on 23.02.15.
 */
public class DataHelper {

    static final private String TAG = "DataHelper";

    static private int BUFFER_SIZE = 1024;

    static public JSONObject stringToJsonObject(String JsonString) {

        JSONObject object;

        try{
             object = new JSONObject(JsonString);
        }
        catch(JSONException e){
            Log.v(TAG, "String could not be processed. Error.");
            object = new JSONObject();
        }

        return object;
    }

    static public String FileToString(File file) {
        String extracted = "";
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String s;
            try {
                while ( (s = buffer.readLine()) != null) {
                    extracted += " " + s;
                    Log.v(TAG,s);
                }
            }
            catch(IOException e) {
                Log.v(TAG,"IOEcxeption! String Extraction aborted!");
            }
        }
        catch(FileNotFoundException e) {
            Log.v(TAG,"File not found. String Extraction aborted!");
        }

        return extracted;
    }

}
