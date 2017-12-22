package com.drone.imavis.mvp.util.listener;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by adigu on 22.12.2017.
 */

public class TaskListener extends AsyncTask<Void, Void, Object> {

    private OnEventListener<Object, Object> mCallBack;
    private Context mContext;
    public Object mException;

    public TaskListener(Context context, OnEventListener callback) {
        mContext = context;
        mCallBack = callback;
    }

    @Override
    protected Object doInBackground(Void... params) {

        try {
            // todo try to do something dangerous
            return "HELLO";
        }
        catch (Exception e) {
            mException = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(result);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}