package gl.iglou.studio.retopo.DATA;

import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

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


    static private String readerToString(BufferedReader reader) {
        String data = "";
        String s;

        try {
            while ( (s = reader.readLine()) != null) {
                data += s;
            }
        }
        catch(IOException e) {
            Log.v(TAG,"IOException! String Extraction aborted!");
        }

        return data;
    }

    static public String fileToString(File file) {
        String extracted = "";
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            extracted = readerToString(buffer);
        }
        catch(FileNotFoundException e) {
            Log.v(TAG,"File not found. String Extraction aborted!");
        }

        return extracted;
    }


    static public String rawToString(InputStream input) {
        String data = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        data = readerToString(reader);

        return data;
    }


    static public JSONObject rawToJSONObject(InputStream input) {

        String data = rawToString(input);
        return stringToJsonObject(data);

    }


    static public JSONObject fileToJSONObject(File file)
    {
        String data = fileToString(file);
        return stringToJsonObject(data);
    }

    static public void printKeys(JSONObject object) {
        Log.v(TAG,"Extracting keys...");
        Iterator<String> keys = object.keys();
        while(keys.hasNext()){
            Log.v(TAG,keys.next());
        }
    }
}
