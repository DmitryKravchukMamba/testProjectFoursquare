package com.example.eklerov2.testprojectfoursquare.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eklerov2.testprojectfoursquare.R;
import com.example.eklerov2.testprojectfoursquare.adapters.PhotoAdapter;

import com.example.eklerov2.testprojectfoursquare.adapters.TipsAdapter;
import com.example.eklerov2.testprojectfoursquare.api.PhotoVenuesAPI;
import com.example.eklerov2.testprojectfoursquare.models.imageModels.ImageModel;

import com.example.eklerov2.testprojectfoursquare.models.imageModels.Item_;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Tip;
import com.example.eklerov2.testprojectfoursquare.utils.BlurTransformationPicasso;
import com.example.eklerov2.testprojectfoursquare.utils.CircleTransformation;
import com.example.eklerov2.testprojectfoursquare.utils.ImageDialog;
import com.example.eklerov2.testprojectfoursquare.utils.Utils;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Evgen on 22.03.16.
 */
public class DetailVenueFragment extends Fragment implements PhotoAdapter.OnItemClickListener, View.OnClickListener {
   private RecyclerView recyclerPhoto;
    private RecyclerView recyclerTips;
    private PhotoAdapter photoAdapter;
    private TipsAdapter tipsAdapter;
    private  Calendar calendar;
    private String venueId;
    private String phone;
    private String rating;
    private String ratingColor;
    private String url;
    private String price;
    private String name;
    private String address;
    private String hour;

    private TextView nameText;
    private TextView adressText;
    private TextView phoneText;
    private TextView urlText;
    private TextView hoursText;
    private TextView reitingText;
    private TextView priceText;
    private TextView noPhotoText;

    private ImageView background;
    private ImageView ratingImage;
    private List<Item_> photoArray = new ArrayList<>();
    private List<Tip> tipArray = new ArrayList<>();
    private RelativeLayout progressLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.venuse_information_fragment_layout, container, false);
        calendar = GregorianCalendar.getInstance();

        venueId = getArguments().getString("venueId");
        address = getArguments().getString("address");
        phone = getArguments().getString("phone");
        rating = getArguments().getString("rating");
        ratingColor = getArguments().getString("ratingColor");
        url = getArguments().getString("url");
        price = getArguments().getString("price");
        name = getArguments().getString("name");
        hour = getArguments().getString("hour");
        initUi(view);
        fetсhPhotoVenuse(venueId);


        photoAdapter = new PhotoAdapter(getActivity(), photoArray);
        tipsAdapter = new TipsAdapter(getActivity(), tipArray);
        recyclerTips.setAdapter(tipsAdapter);
   recyclerTips.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        photoAdapter.setOnItemClickListener(this);
        recyclerPhoto.setAdapter(photoAdapter);
        recyclerPhoto.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        return view;
    }
public void setTipsArray(ArrayList<Tip> tipsList){
    tipArray.addAll(tipsList);
}
    private void initUi(View view) {
        progressLayout = (RelativeLayout) view.findViewById(R.id.freelancerListProgressBarWrapper);
        nameText = (TextView) view.findViewById(R.id.name_venuse_id);
        adressText = (TextView) view.findViewById(R.id.address_text_id);
        phoneText = (TextView) view.findViewById(R.id.phone_text_id);
        urlText = (TextView) view.findViewById(R.id.url_text_id);
        hoursText = (TextView) view.findViewById(R.id.hour_text_id);
        reitingText = (TextView) view.findViewById(R.id.reting_id);
        priceText = (TextView) view.findViewById(R.id.price_text_id);
        noPhotoText = (TextView) view.findViewById(R.id.no_photo_id);

        priceText.setText(price);
        nameText.setText(name);
        adressText.setText(address);
        phoneText.setText(phone);
        urlText.setText(url);
        hoursText.setText(hour);
        reitingText.setText(rating);
        if (ratingColor != null) {
            reitingText.setTextColor(Color.parseColor("#" + ratingColor));
        }
        urlText.setOnClickListener(this);
        phoneText.setOnClickListener(this);

        background = (ImageView) view.findViewById(R.id.background_image_id);
        ratingImage = (ImageView) view.findViewById(R.id.rating_image_id);
        if (rating == null) {
            ratingImage.setVisibility(View.INVISIBLE);
        }

        recyclerPhoto = (RecyclerView) view.findViewById(R.id.recycler_photo_id);
        recyclerTips = (RecyclerView) view.findViewById(R.id.recycler_tips_id);
    }

    @Override
    public void onItemClick(PhotoAdapter.ItemHolder item, int position, int id) {
        showImage(position);
    }


    private void fetсhPhotoVenuse(String idVenue) {
        showProgress();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.foursquare.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PhotoVenuesAPI photoVenuesAPI = retrofit.create(PhotoVenuesAPI.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("v", Utils.getCurrentDate(calendar));
        params.put("oauth_token", Hawk.get("myToken").toString());
        params.put("limit", "100");

        Call<ImageModel> call = photoVenuesAPI.loadPhoto(idVenue, params);
        call.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Response<ImageModel> response, Retrofit retrofit) {
                hideProgress();
                photoArray.addAll(response.body().getResponse().getPhotos().getItems());
                photoAdapter.setItems(photoArray);

                if(photoArray.size()!=0) {
                    Picasso.with(getActivity())
                            .load(Utils.urlBilder(photoArray.get(0).getPrefix(), photoArray.get(0).getSuffix(), "400x400"))
                            .transform(new BlurTransformationPicasso(getActivity(), 10))
                            .into(background);
                }else{
                      noPhotoText.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Throwable t) {
                hideProgress();
            }
        });


    }


    private void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }


    private void showImage(int position) {
        Dialog settingsDialog = new Dialog(getActivity());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ImageView photo;
        settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.image_dialog_layout, null));
        photo = (ImageView) settingsDialog.findViewById(R.id.photo_image);
        Picasso.with(getActivity())
                .load(Utils.urlBilder(photoArray.get(position).getPrefix(), photoArray.get(position).getSuffix(),"700x700"))
                .into(photo);
        settingsDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.url_text_id:
                Intent webIntent = new Intent();
                webIntent.setAction(Intent.ACTION_VIEW);
                webIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                webIntent.setData(Uri.parse(urlText.getText().toString()));
                startActivity(webIntent);
                break;
            case R.id.phone_text_id:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneText.getText().toString(), null));
                startActivity(intent);
                break;
        }
    }
}
