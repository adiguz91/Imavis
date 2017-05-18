package com.drone.imavis.mvp.ui.login;

import com.drone.imavis.mvp.ui.base.IMvpView;

/**
 * Created by adigu on 17.05.2017.
 */
public interface ILoginMvpView extends IMvpView {

    void onLoginSuccess();
    void onLoginFailed();
}
