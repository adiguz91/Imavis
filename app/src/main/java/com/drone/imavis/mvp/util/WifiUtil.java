package com.drone.imavis.mvp.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.thanosfisherman.wifiutils.WifiUtils;

import javax.inject.Inject;

@PerActivity
public class WifiUtil {

    private Context context;
    private PreferencesHelper preferencesHelper;
    private IWifiUtilCallback wifiUtilCallback; // TODO callback onSuccess, onFail

    private View positiveAction;
    private EditText passwordInput;

    @Inject
    public WifiUtil(@ActivityContext Context context, PreferencesHelper preferencesHelper) {
        this.context = context;
        this.preferencesHelper = preferencesHelper;
    }

    public void setWifiUtilCallback(IWifiUtilCallback wifiUtilCallback) {
        this.wifiUtilCallback = wifiUtilCallback;
    }

    public void showDialogConnect(ScanResult scanResult) {
        MaterialDialog dialog =
                new MaterialDialog.Builder(context)
                        .title("Wifi connector")
                        .customView(R.layout.dialog_wifi_connector, true)
                        .positiveText("Connect")
                        .negativeText(android.R.string.cancel)
                        .onPositive(
                                (dialog1, which) -> {
                                    EditText passwordInput = dialog1.getCustomView().findViewById(R.id.password);
                                    onWifiConnect(scanResult.SSID, passwordInput.getText().toString());
                                })
                        .build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions

        TextView textViewSSID = dialog.getCustomView().findViewById(R.id.textViewSSID);
        textViewSSID.setText(scanResult.SSID);
        TextView textViewSecurity = dialog.getCustomView().findViewById(R.id.textViewSecurity);
        textViewSecurity.setText(scanResult.capabilities);
        TextView textViewSignalStrength = dialog.getCustomView().findViewById(R.id.textViewSignalStrength);
        int signalStrengthLevel = WifiManager.calculateSignalLevel(scanResult.level, SignalStrength.values().length);
        textViewSignalStrength.setText(SignalStrength.values()[signalStrengthLevel].toString());

        passwordInput = dialog.getCustomView().findViewById(R.id.password);
        passwordInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        positiveAction.setEnabled(s.toString().trim().length() > 0);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        // Toggling the show password CheckBox will mask or unmask the password input EditText
        CheckBox checkbox = dialog.getCustomView().findViewById(R.id.showPassword);
        checkbox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    passwordInput.setInputType(
                            !isChecked ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                    passwordInput.setTransformationMethod(
                            !isChecked ? PasswordTransformationMethod.getInstance() : null);
                });

        int widgetColor = ThemeSingleton.get().widgetColor;
        MDTintHelper.setTint(
                checkbox, widgetColor == 0 ? ContextCompat.getColor(context, R.color.accent) : widgetColor);

        MDTintHelper.setTint(
                passwordInput,
                widgetColor == 0 ? ContextCompat.getColor(context, R.color.accent) : widgetColor);

        positiveAction.setEnabled(false); // disabled by default
        dialog.show();
        //positiveAction.setEnabled(false); // disabled by default
    }

    private void onWifiConnect(String ssid, String password) {
        // connect to wifi
        WifiUtils.withContext(context)
                .connectWith(ssid, password)
                .onConnectionResult(isSuccess -> {
                    if (isSuccess) {
                        //showToast("Connection succeeded!");
                        preferencesHelper.setDroneWifiSsid(ssid);
                        wifiUtilCallback.onSuccess();
                    } else {
                        //showToast("Connection failed!");
                        wifiUtilCallback.onFail();
                    }
                }).start();
    }
}
