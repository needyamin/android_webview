package com.needyamin.ansnew;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.onesignal.OneSignal;

public class MainActivity extends Activity {
    WebView webView;
    ProgressBar progressBar;

    //WebView Main URL
    String URL = "https://www.ansnew.com/";
    //String URL = "https://gs.statcounter.com/detect/";
    //AnsNew User-Agent
    String MyUA = "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19" +
            "(KHTML, like Gecko; googleweblight) Chrome/38.0.1025.166 Mobile Safari/535.19";
    //OneSignal API
    private static final String ONESIGNAL_APP_ID = "1f032a19-8d98-4e32-9795-2273d09a4a9e";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        //load custom User-agent string
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setUserAgentString(MyUA);

        //load process bar on the webview
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView)findViewById(R.id.webView);

        //JavaScript Enabled on Webview
        webView.getSettings().setJavaScriptEnabled(true);

        //loading progressbar
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                progressBar.setProgress(progress);

                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });

        ////////// check internet connection start //////////
        if (isOnline()) {
            webView.loadUrl(URL);
            Toast.makeText(getApplicationContext(), "Welcome to AnsNew", Toast.LENGTH_SHORT).show();

        } else {
            try {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Error")
                        .setMessage("Internet not available, Please check your internet connectivity or try again later...")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();

                            }
                        }).show();
            } catch (Exception e) {
                //Log.d(Constants.TAG, "Show Dialog: " + e.getMessage());
            }
        }
        ////////// check internet connection end //////////

    }

    //enabling back button to go to previous page
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }


    //enabling back button to go to previous page
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}