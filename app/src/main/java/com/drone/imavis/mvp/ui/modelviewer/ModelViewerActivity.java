package com.drone.imavis.mvp.ui.modelviewer;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dd.processbutton.iml.ActionProcessButton;
import com.drone.imavis.mvp.R;
import com.drone.imavis.mvp.ui.base.BaseActivity;
import com.drone.imavis.mvp.ui.login.ILoginMvpView;
import com.drone.imavis.mvp.ui.login.LoginPresenter;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by adigu on 09.09.2017.
 */

public class ModelViewerActivity extends BaseActivity implements IModelViewerActivity {

    @Inject
    ModelViewerPresenter modelViewerPresenter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_model_viewer);
        ButterKnife.bind(this);
        context = this;

        //loadWebView();

        modelViewerPresenter.attachView(this);
    }

    private void loadCrosswalk() {
        XWalkView xWalkWebView=(XWalkView)findViewById(R.id.modelViewerWebView);
        xWalkWebView.load("http://192.168.99.100:8000/3d/project/38/task/3/", null);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
    }

    private void loadWebView() {
        final WebView webview = (WebView)findViewById(R.id.modelViewerWebView);
        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                webview.loadUrl(
                    "javascript: (function() { " +
                       // "document.addEventListener('DOMContentLoaded', function(event) {" +
                            "document.getElementById('potree_render_area').style.left = '0px';" +
                            "document.getElementById('navbar-top').style.display='none'; " +
                            "document.getElementsByClassName('content')[0].getElementsByTagName('h3')[0].style.display = 'none'; " +
                            "document.getElementsByClassName('content')[0].style.padding = '0px'; " +
                            "document.getElementsByClassName('content')[0].getElementsByTagName('div')[0].style.height = '100%'; " +
                            "document.getElementById('page-wrapper').style.padding = '0px'; " +
                        //"});" +
                    "})();");
            }
        });
        webview.loadUrl("http://192.168.99.100:8000/3d/project/38/task/3/");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        modelViewerPresenter.detachView();
    }
}
