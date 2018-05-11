package com.drone.imavis.mvp.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.di.ActivityContext;
import com.drone.imavis.mvp.di.PerActivity;

import javax.inject.Inject;

@PerActivity
public class LoadingDialogUtil {

    private Context context;
    private MaterialDialog dialog;

    @Inject
    public LoadingDialogUtil(@ActivityContext Context context) {
        this.context = context;
    }

    public void show() {
        dialog = new MaterialDialog.Builder(context)
                //.title("Loading")
                .customView(R.layout.dialog_loading, false)
                .backgroundColorRes(R.color.transparentWhite80) //like this
                .cancelable(false)
                .build();

        // change layout width
        int width = 98;
        //int height = 98;
        ViewGroup.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, context.getResources().getDisplayMetrics());
        //params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.getResources().getDisplayMetrics());
        dialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        //dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        dialog.show();
    }

    public void close() {
        dialog.cancel();
    }

}
