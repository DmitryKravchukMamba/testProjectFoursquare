package com.example.eklerov2.testprojectfoursquare.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.eklerov2.testprojectfoursquare.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    public static final String BROADCAST_ACTION = "get location";
    public static final long NOTIFY_INTERVAL = 1000 * 60 * 2; // 1 min
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mOldLocation;
    Intent intent;
    WifiManager wifi;
    LocationManager mlocManager;

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initTimer();


    }

    private void initTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        return START_STICKY;
    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "ttttttttt",
                            Toast.LENGTH_SHORT).show();
                    if(wifi.isWifiEnabled() && mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        runGooglePlayServices();
                    }else{
                        Toast.makeText(getApplicationContext(), getString(R.string.check_internet_connection),
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    private void runGooglePlayServices() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (mOldLocation == null) {
                            mOldLocation = new Location("manual");
                            mOldLocation = mLastLocation;
                            sendToMyBroadcast();
                        } else if (mLastLocation.getLatitude() != mOldLocation.getLatitude() || mLastLocation.getLongitude() != mOldLocation.getLongitude()) {
                            mOldLocation = mLastLocation;
                            sendToMyBroadcast();

                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();


    }

    private void sendToMyBroadcast() {
        intent.putExtra("Latitude", mLastLocation.getLatitude());
        intent.putExtra("Longitude", mLastLocation.getLongitude());
        intent.putExtra("Provider", mLastLocation.getProvider());
        sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");

    }


}