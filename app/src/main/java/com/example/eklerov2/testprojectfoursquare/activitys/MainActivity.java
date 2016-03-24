package com.example.eklerov2.testprojectfoursquare.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eklerov2.testprojectfoursquare.Constants;
import com.example.eklerov2.testprojectfoursquare.R;
import com.example.eklerov2.testprojectfoursquare.adapters.VenuesListAdapter;
import com.example.eklerov2.testprojectfoursquare.api.FoursquareAPI;
import com.example.eklerov2.testprojectfoursquare.api.GetToken;
import com.example.eklerov2.testprojectfoursquare.fragments.DetailVenueFragment;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Item_;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.PlaceObject;

import com.example.eklerov2.testprojectfoursquare.utils.SortVenueList;
import com.example.eklerov2.testprojectfoursquare.utils.Utils;

import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;

import com.google.gson.JsonObject;
import com.orhanobut.hawk.Hawk;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<PlaceObject>, VenuesListAdapter.OnItemClickListener,View.OnClickListener {
    public static final int DEFAULT_OFFSET = 0;
    String query = "pizza";
    RecyclerView venuesList;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout progressLayout;

    protected GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mOldLocation;

    boolean isRefresh = false;
    boolean isNewYork = false;
    boolean isRefreshLayout = false;
    double latitudeNewYork = 40.7486287;
    double longitudeNewYork = -74.021431;

    Calendar calendar;

    ConnectivityManager connectivityManager;
    VenuesListAdapter venuesListAdapter;
    List<Item_> venueArray = new ArrayList<>();

    LocationManager mlocManager;
    private LinearLayoutManager linearLayoutManager;
LinearLayout newYorkBtn;
TextView newYorkText;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int loadMoreOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
     newYorkBtn = (LinearLayout) findViewById(R.id.button_new_york);
        newYorkBtn.setOnClickListener(this);
         newYorkText = (TextView) findViewById(R.id.new_york_text);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        calendar = GregorianCalendar.getInstance();
        progressLayout = (RelativeLayout) findViewById(R.id.freelancerListProgressBarWrapper);
        venuesList = (RecyclerView) findViewById(R.id.list_venuse_id);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        venuesList.setLayoutManager(linearLayoutManager);
        venuesListAdapter = new VenuesListAdapter(this, venueArray);
        venuesList.setAdapter(venuesListAdapter);
        venuesListAdapter.setOnItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setProgressViewOffset(true, 50, 150);

        turnGPSOn();
        runGooglePlayServices();


        venuesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            loadMoreOffset = loadMoreOffset + 10;
                            if (isNewYork) {
                                fetсhLoadMoreVenuse(latitudeNewYork, longitudeNewYork, loadMoreOffset);
                            } else {
                                fetсhLoadMoreVenuse(mLastLocation.getLatitude(), mLastLocation.getLongitude(), loadMoreOffset);
                            }
                        }
                    }
                }
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                loading = true;
                isRefreshLayout = true;
                fetchMyPlaces();

            }

        });

    }

    private void fetchMyPlaces() {
        turnGPSOn();
        pastVisiblesItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        loadMoreOffset = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(isNewYork){
            fetсhVenuse(latitudeNewYork, longitudeNewYork, DEFAULT_OFFSET);
        }else {
            fetсhVenuse(mLastLocation.getLatitude(), mLastLocation.getLongitude(), DEFAULT_OFFSET);
        }
    }


    public List<Item_> sortedVenueList(List<Item_> list) {
        Collections.sort(list, new SortVenueList());
        return list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void fetсhLoadMoreVenuse(double lat, double lon, int offset) {

        if (venueArray != null && venueArray.size() > 0) {
            if (venueArray.get(venueArray.size() - 1) != null) {
                venueArray.add(null);
                venuesListAdapter.setItems(venueArray);
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoursquareAPI foursquareAPI = retrofit.create(FoursquareAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("v", Utils.getCurrentDate(calendar));
        params.put("oauth_token", Hawk.get("myToken").toString());
        params.put("ll", lat + "," + lon);
        params.put("offset", offset + "");
        params.put("limit", "10");
        params.put("radius", "3000");
        params.put("query", query);


        Call<PlaceObject> call = foursquareAPI.loadQuestions(params);
        call.enqueue(new Callback<PlaceObject>() {
            @Override
            public void onResponse(Response<PlaceObject> response, Retrofit retrofit) {


                if (venueArray.size() > 0) {
                    if (venueArray.get(venueArray.size() - 1) == null) {
                        venueArray.remove(venueArray.size() - 1);
                    }
                    if (response.body().getResponse().getGroups().get(0).getItems().size() != 0) {
                        venueArray.addAll(response.body().getResponse().getGroups().get(0).getItems());
                        loading = true;
                        venuesListAdapter.setItems(sortedVenueList(venueArray));
                    } else {
                        loading = false;
                        venuesListAdapter.setItems(sortedVenueList(venueArray));
                    }
                }


            }

            @Override
            public void onFailure(Throwable t) {
                if (venueArray.size() > 0) {
                    if (venueArray.get(venueArray.size() - 1) == null) {
                        venueArray.remove(venueArray.size() - 1);
                        venuesListAdapter.setItems(sortedVenueList(venueArray));
                    }
                    loading = true;
                }

            }
        });
    }


    private void fetсhVenuse(double lat, double lon, int offset) {
        if (isRefreshLayout == false) {
            showProgress();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FoursquareAPI foursquareAPI = retrofit.create(FoursquareAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("oauth_token", Hawk.get("myToken").toString());
        params.put("v", Utils.getCurrentDate(calendar));
        params.put("ll", lat + "," + lon);
        params.put("offset", offset + "");
        params.put("limit", "10");
        params.put("radius", "3000");
        params.put("query", query);


        Call<PlaceObject> call = foursquareAPI.loadQuestions(params);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Response<PlaceObject> response, Retrofit retrofit) {
        hideProgress();
        turnGPSOff();
        swipeRefreshLayout.setRefreshing(false);
        if (isRefresh || isRefreshLayout) {
            venueArray.removeAll(venueArray);
            isRefresh = false;
            isRefreshLayout = false;
        }
        venueArray.addAll(response.body().getResponse().getGroups().get(0).getItems());
        venuesListAdapter.setItems(sortedVenueList(venueArray));

    }

    @Override
    public void onFailure(Throwable t) {
        isRefresh = false;
        hideProgress();
        turnGPSOff();
        swipeRefreshLayout.setRefreshing(false);

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
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (mOldLocation == null) {
                            mOldLocation = new Location("manual");
                            mOldLocation = mLastLocation;
                            isRefresh = true;
                            fetсhVenuse(mLastLocation.getLatitude(), mLastLocation.getLongitude(), DEFAULT_OFFSET);

                        } else if (Utils.roundLocation(mLastLocation.getLatitude()) != Utils.roundLocation(mOldLocation.getLatitude()) ||
                                Utils.roundLocation(mLastLocation.getLongitude()) != Utils.roundLocation(mOldLocation.getLongitude())) {
                            mOldLocation = mLastLocation;
                            isRefresh = true;
                            isNewYork = true;
                            loading = true;
                            newYorkText.setText(getString(R.string.new_york));
                            fetсhVenuse(mLastLocation.getLatitude(), mLastLocation.getLongitude(), DEFAULT_OFFSET);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })

                .addApi(LocationServices.API)
                .build();


    }

    private void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }


    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            ifGpsIsEnabled();
        }
    }

    private void turnGPSOff() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (provider.contains("gps")) { //if gps is enabled
            ifGpsIsEnabled();
        }
    }

    private void ifGpsIsEnabled() {
        final Intent poke = new Intent();
        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3"));
        sendBroadcast(poke);
    }



    @Override
    protected void onStop() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        turnGPSOff();
        super.onStop();
    }

    @Override
    protected void onResume() {

        turnGPSOn();
        if (chekConnection()) {
            mGoogleApiClient.connect();
        } else {
            showNoCheckConnectionDialog();
        }

        super.onResume();
    }

    @Override
    public void onItemClick(VenuesListAdapter.ItemHolder item, int position, int id) {

        if (chekConnection()) {
            goToDetailsFragment(position);
        } else {
            showNoCheckConnectionDialog();
        }
    }

    private void goToDetailsFragment(int position) {
        DetailVenueFragment detailVenueFragment = (DetailVenueFragment) getSupportFragmentManager().findFragmentByTag(Constants.DETAIL_VENUE_FRAGMENT);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (detailVenueFragment != null) {
            ft.remove(detailVenueFragment);
        }
        detailVenueFragment = new DetailVenueFragment();
        Bundle bundle = new Bundle();
        setBandle(position, bundle);
        detailVenueFragment.setTipsArray((ArrayList) venueArray.get(position).getTips());
        detailVenueFragment.setArguments(bundle);
        ft.add(android.R.id.content, detailVenueFragment, Constants.DETAIL_VENUE_FRAGMENT);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void setBandle(int position, Bundle bundle) {
        if (venueArray.get(position).getVenue().getId() != null) {
            bundle.putString("venueId", venueArray.get(position).getVenue().getId());
        }
        if (venueArray.get(position).getVenue().getLocation().getAddress() != null) {
            bundle.putString("address", venueArray.get(position).getVenue().getLocation().getAddress());
        }
        if (venueArray.get(position).getVenue().getContact().getFormattedPhone() != null) {
            bundle.putString("phone", venueArray.get(position).getVenue().getContact().getFormattedPhone());
        }
        if (venueArray.get(position).getVenue().getRating() != null) {
            bundle.putString("rating", venueArray.get(position).getVenue().getRating().toString());
        }
        if (venueArray.get(position).getVenue().getRatingColor() != null) {
            bundle.putString("ratingColor", venueArray.get(position).getVenue().getRatingColor());
        }
        if (venueArray.get(position).getVenue().getPrice().getMessage() != null) {
            bundle.putString("price", venueArray.get(position).getVenue().getPrice().getMessage());
        }
        if (venueArray.get(position).getVenue().getName() != null) {
            bundle.putString("name", venueArray.get(position).getVenue().getName());
        }
        if (venueArray.get(position).getVenue().getHours() != null) {
            bundle.putString("hour", venueArray.get(position).getVenue().getHours().getStatus());
        }
        if (venueArray.get(position).getVenue().getUrl() != null) {
            bundle.putString("url", venueArray.get(position).getVenue().getUrl());
        }
    }


    private void IamInCurrentLocation() {
        isNewYork = false;
        venueArray.removeAll(venueArray);
        venuesListAdapter.setItems(sortedVenueList(venueArray));
        fetchMyPlaces();
    }

    private void IamInNewYork() {
        loading = true;
        isNewYork = true;
        venueArray.removeAll(venueArray);
        venuesListAdapter.setItems(sortedVenueList(venueArray));
        loadMoreOffset = 0;
        fetсhVenuse(latitudeNewYork, longitudeNewYork, DEFAULT_OFFSET);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_new_york:
                if (!isNewYork) {
                    newYorkText.setText(getString(R.string.i_am));
                    IamInNewYork();
                } else {
                    loading = true;
                    newYorkText.setText(getString(R.string.new_york));
                    IamInCurrentLocation();
                }
                break;
        }
    }
}



