package gl.iglou.studio.retopo.DATA;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by metatomato on 23.02.15.
 */
public class FileSystemHelper {

    static final private String TAG = "FielSystemHelper";

    static public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    static public File LoadOrCreatePath(String root_path)
    {
        final String PATH = Environment.getExternalStorageDirectory() + "/" + root_path ;
        Log.v(TAG, "Checking App File System at path " + PATH);

        File rootFile = new File(PATH);

        if(!rootFile.exists()){
            rootFile.mkdirs();
            Log.v(TAG, PATH + " created");
        }

        return rootFile;
    }

    static public ArrayList<File> getFilesByExtension(File rootFile, String extension) {
        ArrayList<File> files = new ArrayList<>();
        for(File file : rootFile.listFiles()) {
            String name = file.getName();
            if( name.endsWith(extension) ) {
                Log.v(TAG, file.getName());
                files.add(file);
            }
        }
        return files;
    }

}
