package com.drone.imavis.mvp.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.drone.imavis.mvp.di.ActivityContext;
import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.di.PerActivity;
import com.drone.imavis.mvp.util.file.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by adigu on 23.02.2017.
 */

@Singleton
public class FileUtil {

    private Context context;

    @Inject
    public FileUtil(@ApplicationContext Context context) {
        this.context = context;
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(context, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(context.getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /*
    * fileFilter == null -> accept all
    * */
    public List<MultipartBody.Part> getFileParts(String partName, Uri folderUri, FileFilter fileFilter) {

        if(fileFilter == null) {
            fileFilter = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return true;
                }
            };
        }

        File folderFile = new File(folderUri.getPath());
        List<File> files = Arrays.asList(folderFile.listFiles(fileFilter));

        List<MultipartBody.Part> fileParts = new ArrayList<>();
        for (File file : files) {
            Uri fileUri = Uri.fromFile(file);
            if (fileUri != null)
                fileParts.add(prepareFilePart(partName, fileUri));
        }
        return fileParts;
    }

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

    public static String readAssetFile(Context context, String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path)));
            String mLine;
            while ((mLine = reader.readLine()) != null) {}
            return mLine;
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return null;
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

    public static String FilePathCombine (String pathA, String pathB)
    {
        File file1 = new File(pathA);
        File file2 = new File(file1, pathB);
        return file2.getAbsolutePath();
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
