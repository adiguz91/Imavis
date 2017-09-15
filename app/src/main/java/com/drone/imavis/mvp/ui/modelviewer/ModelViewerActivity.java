package com.drone.imavis.mvp.ui.modelviewer;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.dd.processbutton.iml.ActionProcessButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.login.ILoginMvpView;
import com.drone.imavis.mvp.ui.login.LoginPresenter;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.xwalk.core.XWalkInitializer;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkSettings;
import org.xwalk.core.XWalkUpdater;
import org.xwalk.core.XWalkView;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by adigu on 09.09.2017.
 */

public class ModelViewerActivity extends BaseActivity implements IModelViewerActivity, XWalkInitializer.XWalkInitListener, XWalkUpdater.XWalkBackgroundUpdateListener {

    @Inject
    ModelViewerPresenter modelViewerPresenter;

    @BindView(R.id.modelViewerWebView)
    XWalkView xWalkWebView;

    //@BindView(R.id.modelViewerWebView)
    private WebView webview;

    private Context context;

    private XWalkInitializer mXWalkInitializer;
    private XWalkUpdater mXWalkUpdater;

    //@BindView(R.id.modelViewerOverlay)
    //LinearLayout overlayView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Must call initAsync() before anything that involes the embedding
        // API, including invoking setContentView() with the layout which
        // holds the XWalkView object.
        mXWalkInitializer = new XWalkInitializer(this, this);
        mXWalkInitializer.initAsync();

        activityComponent().inject(this);
        setContentView(R.layout.activity_model_viewer);
        ButterKnife.bind(this);
        context = this;

        // Until onXWalkInitCompleted() is invoked, you should do nothing
        // with the embedding API except the following:
        // turn on debugging, disable line in release
        //XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        // Call XWalkView.setUIClient()
        // Call XWalkView.setResourceClient()


        xWalkWebView.setResourceClient(
                new XWalkResourceClient(xWalkWebView)
                {
                    @Override
                    public void onLoadFinished(XWalkView xWalkView, String url)
                    {
                        //xWalkView.load("javascript:functionInTest()", null);
                        ValueCallback<String> callback =
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String jsonResult)
                                    {
                                        Log.i("TAG modelviewer TEST","from js:"+jsonResult);
                                        xWalkWebView.onShow();
                                        // Show status bar
                                        //showStatusBar();
                                        //startTimer();
                                    }
                                };
                        xWalkView.evaluateJavascript(
                                "(function() { " +
                                    //"document.addEventListener('DOMContentLoaded', function(event) {" +
                                        "document.getElementById('potree_render_area').style.left = '0px';" +
                                        "document.getElementById('navbar-top').style.display='none'; " +
                                        "document.getElementsByClassName('content')[0].getElementsByTagName('h3')[0].style.display = 'none'; " +
                                        "document.getElementsByClassName('content')[0].style.padding = '0px'; " +
                                        "document.getElementsByClassName('content')[0].getElementsByTagName('div')[0].style.height = '100%'; " +
                                        "document.getElementById('page-wrapper').style.padding = '0px'; " +
                                    //"});"
                                "})();", callback);
                    }
                }
        );

        modelViewerPresenter.attachView(this);
    }

    private void loadCrosswalk() {
        xWalkWebView.onHide();

        //xWalkWebView.getSettings().setInitialPageScale(100);
        xWalkWebView.load("http://192.168.99.100:8000/3d/project/38/task/3/", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        modelViewerPresenter.detachView();
    }

    @Override
    public void onXWalkInitStarted() {

    }

    @Override
    public void onXWalkInitCancelled() {
        // Perform error handling here
    }

    @Override
    public void onXWalkInitFailed() {
        // Perform error handling here, or launch the XWalkUpdater
        if (mXWalkUpdater == null) {
            mXWalkUpdater = new XWalkUpdater(this, this);
        }

        // The updater won't be launched if previous update dialog is
        // showing.
        mXWalkUpdater.updateXWalkRuntime();
    }

    @Override
    public void onXWalkInitCompleted() {
        loadCrosswalk();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Try to initialize again when the user completed updating and
        // returned to current activity. The initAsync() will do nothing if
        // the initialization has already been completed successfully.
        mXWalkInitializer.initAsync();
    }

    @Override
    public void onXWalkUpdateStarted() {
    }

    @Override
    public void onXWalkUpdateProgress(int percentage) {
    }

    @Override
    public void onXWalkUpdateCancelled() {
        finish();
    }

    @Override
    public void onXWalkUpdateFailed() {
        finish();
    }

    @Override
    public void onXWalkUpdateCompleted() {
        mXWalkInitializer.initAsync();

    }

    // Hide Status Bar
    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Hide Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    // Show Status Bar
    public void showStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

/*
    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();

    private void stopTimer(){
        if(mTimer1 != null){
            mTimer1.cancel();
            mTimer1.purge();
        }
    }

    private void startTimer(){
        mTimer1 = new Timer();
        mTt1 = new TimerTask() {
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run(){
                        showStatusBar();
                        xWalkWebView.leaveFullscreen();
                    }
                });
            }
        };

        mTimer1.schedule(mTt1, 1, 5000);
    }
*/
}