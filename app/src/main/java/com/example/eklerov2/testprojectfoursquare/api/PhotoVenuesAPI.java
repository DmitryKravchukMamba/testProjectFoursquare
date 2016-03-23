package com.example.eklerov2.testprojectfoursquare.api;

import com.example.eklerov2.testprojectfoursquare.models.imageModels.ImageModel;


import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by Evgen on 22.03.16.
 */
public interface PhotoVenuesAPI {
    @GET("/v2/venues/{venue_id}/photos?")
    Call<ImageModel> loadPhoto(@Path("venue_id") String venue_id,@QueryMap Map<String, String> params);



}
