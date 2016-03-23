package com.example.eklerov2.testprojectfoursquare.adapters;

/**
 * Created by Evgen on 19.03.16.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.eklerov2.testprojectfoursquare.R;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Item_;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Venue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class VenuesListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private OnItemClickListener onItemClickListener;
    private List<Item_> items;

    private Venue venue;

    final int VIEW_PROG=0;
 final int TYPE_ITEM = 1;




    public void setItems(List<Item_> items){
        this.items=items;
        notifyDataSetChanged();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView nameVenue;
        public TextView distance;
        private RelativeLayout venueLayout;
        private VenuesListAdapter parent;

        public ItemHolder(View v, VenuesListAdapter parent) {
            super(v);
            this.parent = parent;
            venueLayout = (RelativeLayout)v.findViewById(R.id.venue_layout_id);
            nameVenue = (TextView)v.findViewById(R.id.name_venue_id);
            distance= (TextView)v.findViewById(R.id.distance_id);
            venueLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                listener.onItemClick(this, getPosition(), v.getId());
            }
        }
    }

    public VenuesListAdapter(Activity activity, List<Item_> items) {
        this.items = items;
        this.activity=activity;
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
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

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
            vh = new ItemHolder(v, this);
            return vh;
        } else if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(view);
        }
        return vh;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemHolder){
        venue = items.get(position).getVenue();
        ((ItemHolder)holder).nameVenue.setText(venue.getName());
        ((ItemHolder)holder).distance.setText(venue.getLocation().getDistance()+" "+"m");
    }  else if (holder instanceof ProgressViewHolder) {
        ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
    }

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(items.get(position)==null){
            return VIEW_PROG;
        }else {
            return TYPE_ITEM;
        }
    }

}

