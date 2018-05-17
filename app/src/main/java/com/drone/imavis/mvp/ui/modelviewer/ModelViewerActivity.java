package com.drone.imavis.mvp.ui.modelviewer;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.ValueCallback;

import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.util.LoadingDialogUtil;

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
    @Inject
    LoadingDialogUtil loadingDialogUtil;

    @BindView(R.id.modelViewerWebView)
    XWalkView xWalkWebView;

    private XWalkInitializer mXWalkInitializer;
    private XWalkUpdater mXWalkUpdater;

    private String projectId;
    private String taskId;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_viewer);
        activityComponent().inject(this);
        ButterKnife.bind(this);

        //setupBackButton();
        final Drawable leftArrow = getDrawable(R.drawable.abc_ic_ab_back_material);
        leftArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(leftArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectId = getIntent().getExtras().getString("PROJECT_ID");
        taskId = getIntent().getExtras().getString("TASK_ID");
        username = "admin";
        password = "admin";

        // Must call initAsync() before anything that involes the embedding
        // API, including invoking setContentView() with the layout which
        // holds the XWalkView object.
        mXWalkInitializer = new XWalkInitializer(this, this);
        mXWalkInitializer.initAsync();

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
                                    }
                                };
                        xWalkView.evaluateJavascript(
                                "(function() { " +
                                        //"document.addEventListener('DOMContentLoaded', function(event) {" +
                                        "var url = window.location.href;" +
                                        "var search = 'login';" +
                                        "if(url.includes(search)) {" +
                                        "var form = document.getElementsByTagName('form')[0];" +
                                        "document.getElementById('id_username').value = '" + username + "';" +
                                        "document.getElementById('id_password').value = '" + password + "';" +
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
                                        loadingDialogUtil.close();
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

                    @Override
                    public void onReceivedLoadError(XWalkView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedLoadError(view, errorCode, description, failingUrl);
                        loadingDialogUtil.close();
                    }
                }
        );

        xWalkWebView.setUIClient(new XWalkUIClient(xWalkWebView));

        modelViewerPresenter.attachView(this);
    }

    // backbutton pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish(); // use finished if override onBackPressed() - HW use finish()
                //NavUtils.navigateUpFromSameTask(this);
                //onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadCrosswalk() {
        xWalkWebView.onHide();
        loadingDialogUtil.show();
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
}
