package extensions.file;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by adigu on 23.02.2017.
 */

public class FileExtension {

    private static String getAbsoluteFolderPath(String absoluteFilePath) {
        File file = new File(absoluteFilePath);
        if(file != null && file.isAbsolute()) {
            if(file.isDirectory())
                return absoluteFilePath;
            else
                return file.getParent();
        }
        return null;
    }

    private static void checkProjectDirectories() {
        String rootFolder = "/imavis/";
        String flyPlanFolder = "flyplan";
        String absolutFolderPath = Environment.DIRECTORY_DCIM + rootFolder + flyPlanFolder;
        checkFileDirectory(absolutFolderPath);
    }

    private static boolean checkFileDirectory(String absolutFolderPath) {
        checkProjectDirectories();
        absolutFolderPath = getAbsoluteFolderPath(absolutFolderPath);
        File directoryPath = null;
        if(absolutFolderPath != null) {
            // Get the directory for the user's public pictures directory.
            //Environment.DIRECTORY_PICTURES
            directoryPath = Environment.getExternalStoragePublicDirectory(absolutFolderPath);
            if(!directoryPath.exists()) // Make sure the path directory exists.
                directoryPath.mkdirs(); // Make it, if it doesn't exit

            if(directoryPath != null)
                return true;
        }
        return false;
    }

    private static boolean checkFile(String absoluteFileName) {
        return checkFileDirectory(absoluteFileName);
    }

    public static String readFile(File file) throws Exception {
        if(file != null && file.exists() && file.canRead()) {
            StringBuilder sb = new StringBuilder();
            try
            {
                //File fl = new File(filePath);
                //FileInputStream fin = new FileInputStream(fl);
                //BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                return sb.toString();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean writeToFile(String absoluteFileName, String content)
    {
        try
        {
            if(checkFile(absoluteFileName)) {
                final File file = new File(absoluteFileName);
                // Save your stream, don't forget to flush() it before closing it.
                file.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fileOut);
                myOutWriter.append(content);
                myOutWriter.close();
                fileOut.flush();
                fileOut.close();
                return true;
            }
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return false;
    }
}
