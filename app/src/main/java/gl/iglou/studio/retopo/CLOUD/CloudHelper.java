package gl.iglou.studio.retopo.CLOUD;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by metatomato on 27.03.15.
 */
public class CloudHelper {

    private final String TAG = "CLOUD";

    /**
     * Default constructor
     */
    public CloudHelper() {
    }

    /**
     * Log network received data
     */
    private void logFetchedData(String data) {
        Log.v(TAG,data);
    }

    /**
     * Fetch network data
     */
    public void fetchDataFromUrl(String url) {
        new DownloadTask().execute(url);
    }

    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return "ERROR 404!";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            logFetchedData(result);
        }
    }

    /**w
     * Initiates the fetch operation.
     */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str = "";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     *
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream InputStream containing HTML from targeted site.
     * @param len    Length of string that this method returns.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String readIt(InputStream stream) throws IOException{
        StringBuilder builder = new StringBuilder();

        BufferedReader bufferReader = new BufferedReader( new InputStreamReader(stream, "UTF-8") );
        String line;
        while ((line = bufferReader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}