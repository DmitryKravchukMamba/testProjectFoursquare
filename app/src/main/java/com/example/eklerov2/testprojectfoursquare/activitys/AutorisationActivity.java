package com.example.eklerov2.testprojectfoursquare.activitys;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.eklerov2.testprojectfoursquare.R;
import com.orhanobut.hawk.Hawk;


public class AutorisationActivity extends AppCompatActivity {
    public static final String CALLBACK_URL = "http://www.cossa.ru/news/244/7765/L";
    ProgressDialog progressDialog;
    WebView webview;
    ConnectivityManager connectivityManager;
    boolean isToken = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autorisation_activity_layout);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        webview = (WebView) findViewById(R.id.webView);
        progressDialog = new ProgressDialog(this);

        webview.getSettings().setJavaScriptEnabled(true);


    }

    @Override
    protected void onResume() {

        if (chekConnection()) {
            if (Hawk.get("myToken") == null) {
                getMyToken();
            } else {
                if (isToken == false) {
                    startActivity(new Intent(AutorisationActivity.this, MainActivity.class));
                    finish();
                }
            }
        } else {
            showNoCheckConnectionDialog();
        }

        super.onResume();
    }

    private void getMyToken() {
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        String url = "https://foursquare.com/oauth2/authenticate" +
                "?client_id=" + getString(R.string.client_id) +
                "&response_type=token" +
                "&redirect_uri=" + CALLBACK_URL;

        webview.setWebViewClient(new WebViewClient() {

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    isToken = true;
                    String accessToken = url.substring(start + fragment.length(), url.length());
                    Hawk.put("myToken", accessToken);
                    if(isToken) {
                        startActivity(new Intent(AutorisationActivity.this, MainActivity.class));
                        isToken = false;
                    }
                    finish();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

        });
        webview.loadUrl(url);
    }

    public boolean chekConnection() {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null)
            return false;
        if (!info.isConnected())
            return false;
        if (!info.isAvailable())
            return false;
        return true;
    }

    public void showNoCheckConnectionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getResources().getString(R.string.plase_select_wifi));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }
}
