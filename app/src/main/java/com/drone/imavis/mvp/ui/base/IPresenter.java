package com.drone.imavis.mvp.ui.base;

/**
 * Created by adigu on 10.05.2017.
 */

import android.os.Bundle;

import java.lang.reflect.Type;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface IPresenter<V extends IMvpView> {

    void attachView(V mvpView);

    void detachView();
}