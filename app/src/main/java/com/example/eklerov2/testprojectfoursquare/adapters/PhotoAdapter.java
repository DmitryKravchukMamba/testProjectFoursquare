package com.example.eklerov2.testprojectfoursquare.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eklerov2.testprojectfoursquare.R;
import com.example.eklerov2.testprojectfoursquare.models.imageModels.Item_;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Venue;
import com.example.eklerov2.testprojectfoursquare.utils.CircleTransformation;
import com.example.eklerov2.testprojectfoursquare.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Evgen on 22.03.16.
 */
public class PhotoAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private OnItemClickListener onItemClickListener;
    private List<Item_> items;

    private Item_ photoUrl;


    public void setItems(List<Item_> items){
        this.items=items;
        notifyDataSetChanged();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ImageView photo;

        private PhotoAdapter parent;

        public ItemHolder(View v, PhotoAdapter parent) {
            super(v);
            this.parent = parent;
            photo = (ImageView)v.findViewById(R.id.photo);
            photo.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getPosition(), v.getId());
            }
        }
    }

    public PhotoAdapter(Activity activity, List<Item_> items) {
        this.items = items;
        this.activity=activity;
    }



    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(ItemHolder item, int position, int id);
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh = null;


            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_photo, parent, false);
            vh = new ItemHolder(v, this);
            return vh;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        photoUrl = items.get(position);

        Picasso.with(activity)
                .load(Utils.urlBilder(photoUrl.getPrefix(), photoUrl.getSuffix(),"300x300"))
                .transform(new CircleTransformation())
                .into(((ItemHolder) holder).photo);



    }





    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
            return position;

    }

}


