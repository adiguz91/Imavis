package com.drone.imavis.mvp.util;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.data.local.preference.PreferencesHelper;
import com.drone.imavis.mvp.di.ActivityContext;
import com.drone.imavis.mvp.di.PerActivity;
import com.drone.imavis.mvp.ui.searchwlan.SignalStrength;

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
