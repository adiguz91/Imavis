package com.drone.imavis.mvp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.drone.imavis.mvp.di.ActivityContext;
import com.drone.imavis.mvp.di.ApplicationContext;
import com.drone.imavis.mvp.di.PerActivity;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by adigu on 30.07.2017.
 */

@PerActivity
public class DialogUtil {

    private Context context;

    @Inject
    public DialogUtil(@ActivityContext Context context) {
        this.context = context;
    }

    public void showSimpleDialogMessage(String title, String message, Map<Integer,String> buttons, DialogInterface.OnClickListener dialogOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);

        if(buttons.get(DialogInterface.BUTTON_POSITIVE) != null)
            builder.setPositiveButton(buttons.get(DialogInterface.BUTTON_POSITIVE), dialogOnClickListener);
        if(buttons.get(DialogInterface.BUTTON_NEGATIVE) != null)
            builder.setNegativeButton(buttons.get(DialogInterface.BUTTON_NEGATIVE), dialogOnClickListener);
        if(buttons.get(DialogInterface.BUTTON_NEUTRAL) != null)
            builder.setNeutralButton(buttons.get(DialogInterface.BUTTON_NEUTRAL), dialogOnClickListener);

        builder.show();
    }

    public void showDialog(Context context, String title, List<String> list, String[] btnText,
                           DialogInterface.OnClickListener listener) {

        String[] items = new String[list.size()];
        items = list.toArray(items);

        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {}
                });
        builder.setPositiveButton(btnText[0], listener);
        if (btnText.length != 1) {
            builder.setNegativeButton(btnText[1], listener);
        }
        builder.show();
    }
}
