package com.drone.imavis.mvp.util;

import io.reactivex.disposables.Disposable;

/**
 * Created by adigu on 17.12.2017.
 */

public final class UnsubscribeIfPresent {
    private UnsubscribeIfPresent() {//no instance
    }

    public static void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}