package com.example.eklerov2.testprojectfoursquare.api;

import com.example.eklerov2.testprojectfoursquare.models.venuemodel.PlaceObject;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Evgen on 24.03.16.
 */
public interface GetToken {
    @GET("/oauth2/authenticate?client_id=OGKNEJQHN03HDHSO1P21CPKL0HDP1LDQAF1JARC2U3ZMUK15")
    Call<JsonObject> getToken(@QueryMap Map<String, String> params);

}
