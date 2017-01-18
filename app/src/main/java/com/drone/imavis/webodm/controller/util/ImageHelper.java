package com.drone.imavis.webodm.controller.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Adrian on 30.06.2016.
 */
public class ImageHelper {

    private Bitmap original_bitmap;
    private int target_width;

    public ImageHelper(Bitmap original_bitmap, int width) {
        this.original_bitmap = original_bitmap;
        this.target_width = width;
    }

    public Bitmap scaleAndCompression() {
        //scale
        int target_height = (int) (original_bitmap.getHeight() * target_width / (double) original_bitmap.getWidth());
        Bitmap scaled_bitmap = Bitmap.createScaledBitmap(original_bitmap, target_width, target_height, true);

        // compress
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaled_bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        Bitmap output_bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return output_bitmap;
    }

    /*
     * prepare to send image as encoded string
     */
    public String encodeBitmap(Bitmap input) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        input.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageByteArray = stream.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

}
