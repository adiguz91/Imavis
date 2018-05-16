package com.drone.imavis.mvp.ui.modelviewer;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;

import org.xwalk.core.XWalkInitializer;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkUpdater;
import org.xwalk.core.XWalkView;

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

    private String projectId;
    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectId = getIntent().getExtras().getString("PROJECT_ID");
        taskId = getIntent().getExtras().getString("TASK_ID");

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
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        // Call XWalkView.setUIClient()
        // Call XWalkView.setResourceClient()


        xWalkWebView.setResourceClient(
                new XWalkResourceClient(xWalkWebView) {
                    @Override
                    public void onLoadStarted(XWalkView xWalkView, String url) {
                        ValueCallback<String> callback =
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String jsonResult) {
                                        Log.i("TAG modelviewer TEST", "from js:" + jsonResult);
                                        //xWalkWebView.onHide();
                                        // Show status bar
                                        //showStatusBar();
                                        //startTimer();
                                    }
                                };
                        xWalkView.evaluateJavascript(
                                "(function() { " +
                                        //"document.addEventListener('DOMContentLoaded', function(event) {" +
                                        "var url = window.location.href;" +
                                        "var search = 'login';" +
                                        "if(url.includes(search)) {" +
                                        "var form = document.getElementsByTagName('form')[0];" +
                                        "document.getElementById('id_username').value = 'admin';" +
                                        "document.getElementById('id_password').value = 'admin';" +
                                        "form.submit();" +
                                        "}" +
                                        //"});"
                                        "})();", callback);
                    }

                    @Override
                    public void onLoadFinished(XWalkView xWalkView, String url) {
                        //xWalkView.load("javascript:functionInTest()", null);
                        ValueCallback<String> callback =
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String jsonResult) {
                                        Log.i("TAG modelviewer TEST", "from js:" + jsonResult);
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
                                        "document.getElementById('navbar-top').style.display='none';" +
                                        "document.getElementsByClassName('content')[0].getElementsByTagName('h3')[0].style.display = 'none';" +
                                        "document.getElementsByClassName('content')[0].style.padding = '0px';" +
                                        "document.getElementById('page-wrapper').style.padding = '0px';" +
                                        "document.getElementById('page-wrapper').style.margin = '0px';" +
                                        "document.getElementById('page-wrapper').style.border = '0px';" +
                                        "document.getElementsByTagName('body')[0].style.background = '#0e1619';" +
                                        "document.getElementsByTagName('body')[0].style.height = '100%';" +
                                        "document.getElementById('wrapper').style.height = '100%';" +
                                        "document.getElementsByClassName('full-height')[0].style.padding = '0px';" +
                                        "document.getElementsByClassName('full-height')[0].style.height = '100vh';" +
                                        //"});"
                                        "})();", callback);
                    }
                }
        );

        xWalkWebView.setUIClient(new XWalkUIClient(xWalkWebView));

        modelViewerPresenter.attachView(this);
    }

    private void loadCrosswalk() {
        xWalkWebView.onHide();
        //xWalkWebView.getSettings().setInitialPageScale(100);
        String url = "http://10.0.0.9:8000/3d/project/" + projectId + "/task/" + taskId + "/";
        xWalkWebView.load(url, null);
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
        } else {
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
        } else {
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
