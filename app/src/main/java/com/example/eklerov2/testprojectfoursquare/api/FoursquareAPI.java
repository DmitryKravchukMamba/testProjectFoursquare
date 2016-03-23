package com.example.eklerov2.testprojectfoursquare.api;

import com.example.eklerov2.testprojectfoursquare.models.venuemodel.PlaceObject;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Evgen on 16.03.16.
 */


    public interface FoursquareAPI {
        @GET("/v2/venues/explore?")
        Call<PlaceObject> loadQuestions(@QueryMap Map<String, String> params);
    }
//"client_id=\"+\"OGKNEJQHN03HDHSO1P21CPKL0HDP1LDQAF1JARC2U3ZMUK15\""
