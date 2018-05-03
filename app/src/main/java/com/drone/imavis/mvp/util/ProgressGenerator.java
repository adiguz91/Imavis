package com.drone.imavis.mvp.util;

import android.os.Handler;

import com.dd.processbutton.ProcessButton;

import java.util.Random;

/**
 * Created by adigu on 18.05.2017.
 */

/*
public class ProgressGenerator {

    public interface OnCompleteListener {
        void onComplete();
    }

    private int progress;
    private int timeout;
    private Handler handler;
    private Runnable buttonRunnable;
    private OnCompleteListener listener;
    private ProcessButton processButton;

    public ProgressGenerator(OnCompleteListener listener) {
        this.listener = listener;
    }

    private Runnable CreateRunnableButton(ProcessButton button) {
        return new Runnable() {
            @Override
            public void run() {
                progress += 100 / timeout;
                button.setProgress(progress);
                if (progress < 100)
                    handler.postDelayed(this, 1000); // every second
                else {
                    listener.onComplete();
                }
            }
        };
    }

    // timeout in seconds
    public void start(ProcessButton button, final int timeout) {
        this.progress = 0;
        this.processButton = button;
        this.timeout = timeout * 1000;
        handler = new Handler();
        buttonRunnable = CreateRunnableButton(button);
        handler.postDelayed(buttonRunnable, this.timeout);
    }

    public void stop(int status) {
        if(handler != null && processButton != null) {
            handler.removeCallbacks(buttonRunnable);
            processButton.setProgress(status);
        }
    }

    //public void restart() {
    //    handler.removeCallbacks(buttonRunnable);
    //    start(processButton, this.timeout);
    //}

    private Random random = new Random();
    private int generateDelay() {
        return random.nextInt(1000);
    }
}
*/

public class ProgressGenerator {

    private OnCompleteListener mListener;
    private int mProgress;
    private Random random = new Random();

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button, final long timeout) {
        mProgress = 0;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 10;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, timeout);
    }

    private int generateDelay() {
        return 1100; // one second
        //return random.nextInt(1000);
    }

    public interface OnCompleteListener {

        void onComplete();
    }
}