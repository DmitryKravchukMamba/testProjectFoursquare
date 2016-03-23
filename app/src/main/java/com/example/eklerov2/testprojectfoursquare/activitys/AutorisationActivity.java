package com.example.eklerov2.testprojectfoursquare.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.example.eklerov2.testprojectfoursquare.R;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;
import com.orhanobut.hawk.Hawk;

/**
 * Created by Evgen on 22.03.16.
 */
public class AutorisationActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_FSQ_CONNECT = 1000;
    public static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 2000;
    ConnectivityManager connectivityManager;
    String myToken;
    String mAuthCode;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
                mAuthCode =codeResponse.getCode();
                Hawk.put("mAuthCode", codeResponse.getCode());
                getMyToken();
                break;
            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
                Hawk.put("myToken", tokenResponse.getAccessToken());
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }
    }
    private void getMyToken() {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(this, getString(R.string.client_id),
                getString(R.string.client_secret), mAuthCode);
        startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (chekConnection()) {
            if (Hawk.get("myToken") == null) {
                Intent intentOAuth = FoursquareOAuth.getConnectIntent(this, getString(R.string.client_id));
                startActivityForResult(intentOAuth, REQUEST_CODE_FSQ_CONNECT);
            }else{
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
        } else {
            showNoCheckConnectionDialog();
        }

    }
}
