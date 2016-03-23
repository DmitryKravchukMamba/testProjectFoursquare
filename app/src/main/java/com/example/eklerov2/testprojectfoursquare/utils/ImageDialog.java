package com.example.eklerov2.testprojectfoursquare.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.example.eklerov2.testprojectfoursquare.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Evgen on 23.03.16.
 */
public class ImageDialog  extends DialogFragment {

    private ImageView photoImage;

    public static ImageDialog getInstance(Bundle args){
        ImageDialog instance = new ImageDialog();
        instance.setArguments(args);
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.image_dialog_layout, container);
        photoImage = (ImageView)view.findViewById(R.id.photo_image);
        String img = getArguments().getString("vvv");

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final DisplayMetrics finalDisplaymetrics = displaymetrics;



        Picasso.with(getActivity()).load(img).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int img_width = bitmap.getWidth();
                int img_height = bitmap.getHeight();

                int screen_width = finalDisplaymetrics.widthPixels;
                int screen_height = finalDisplaymetrics.heightPixels;
                int result_width = 0;
                int result_height = 0;
                if (img_width >= img_height) {
                    result_width = screen_width;
                    if (img_width > screen_width) {
                        result_height = img_height - (img_width - screen_width);
                    } else {
                        result_height = (screen_width - img_width) + img_height;
                    }//
                } else {
                    result_height = screen_width;
                    result_width = screen_width - (img_height - img_width);
                }


                android.view.ViewGroup.LayoutParams layoutimageView_3 = photoImage.getLayoutParams();
                layoutimageView_3.width = result_width;
                layoutimageView_3.height = result_height;
                photoImage.setLayoutParams(layoutimageView_3);
                photoImage.setImageBitmap(bitmap);
            }


            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


        return view;
    }





}
