package com.example.eklerov2.testprojectfoursquare.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eklerov2.testprojectfoursquare.R;
import com.example.eklerov2.testprojectfoursquare.models.imageModels.Item_;
import com.example.eklerov2.testprojectfoursquare.models.venuemodel.Tip;
import com.example.eklerov2.testprojectfoursquare.utils.CircleTransformation;
import com.example.eklerov2.testprojectfoursquare.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Evgen on 23.03.16.
 */
public class TipsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private List<Tip> items;

    private Tip tipObject;


    public void setItems(List<Tip> items){
        this.items=items;
        notifyDataSetChanged();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {


        public ImageView photoUser;
        public TextView nameUser;
        public TextView tipsText;


        public ItemHolder(View v) {
            super(v);

            photoUser = (ImageView)v.findViewById(R.id.user_image_id);
            nameUser = (TextView)v.findViewById(R.id.user_name_text_id);
            tipsText = (TextView)v.findViewById(R.id.tips_text_id);



        }


    }

    public TipsAdapter(Activity activity, List<Tip> items) {
        this.items = items;
        this.activity=activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh = null;

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycler_tips, parent, false);
        vh = new ItemHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        tipObject = items.get(position);

        Picasso.with(activity)
                .load(Utils.urlBilder(tipObject.getUser().getPhoto().getPrefix(), tipObject.getUser().getPhoto().getSuffix(), "400x400"))
                .placeholder(R.drawable.image_placeholder_fiolet_darked)
                .transform(new CircleTransformation())
                .into(((ItemHolder) holder).photoUser);

        ((ItemHolder) holder).nameUser.setText(tipObject.getUser().getFirstName());
        ((ItemHolder) holder).tipsText.setText(tipObject.getText());

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


