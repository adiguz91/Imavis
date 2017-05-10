package com.drone.imavis.mvp.util;

import rx.Subscription;

/**
 * Created by adigu on 10.05.2017.
 */

public class RxUtil {

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}