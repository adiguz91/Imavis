package com.drone.imavis.mvp.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.drone.imavis.mvp.di.ActivityContext;
import com.drone.imavis.mvp.di.PerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Created by adigu on 15.08.2017.
 */

@PerActivity
public class ImageUtil {

    private Context context;
    private String directoryPath;
    private String fileName;
    private ImageExtension imageExtension = ImageExtension.PNG;
    private boolean insideAppFolder = true;
    private boolean internal = true;

    // https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android

    @Inject
    public ImageUtil(@ActivityContext Context context) {
        this.context = context;
    }

    public ImageUtil setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        return this;
    }

    public ImageUtil setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ImageUtil setImageExtension(ImageExtension imageExtension) {
        this.imageExtension = imageExtension;
        return this;
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context); // getApplicationContext()
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public String getFilenameWithExtension() {
        return String.format("%1$s.%2$s", fileName, imageExtension.name());
    }

    public Bitmap loadBitmap() {
        Bitmap bitmap = null;
        try {
            File file = new File(directoryPath, getFilenameWithExtension());
            if (file.exists()) {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
