package com.anime.spagreen.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anime.spagreen.AnimeDetailsActivity;
import com.anime.spagreen.AnimePlayerActivity;
import com.anime.spagreen.DetailsActivity;
import com.anime.spagreen.R;
import com.anime.spagreen.models.CommonModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommonGridAdapter extends RecyclerView.Adapter<CommonGridAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;


    public CommonGridAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public CommonGridAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonGridAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image_albums, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommonGridAdapter.OriginalViewHolder holder, final int position) {

        final CommonModels obj = items.get(position);

        holder.name.setText(obj.getTitle());
        Picasso.get().load(obj.getImageUrl()).into(holder.image);

        if (obj.getVideoType().equals("epi")){
            holder.epiName.setVisibility(View.VISIBLE);
            holder.epiName.setText("Episode: "+obj.getEpisodeName());
        }

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (obj.getVideoType().equals("tvseries")){
                    Intent intent=new Intent(ctx, AnimeDetailsActivity.class);
                    intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());
                    ctx.startActivity(intent);
                }else if (obj.getVideoType().equals("epi")){

                    Intent intent=new Intent(ctx, AnimePlayerActivity.class);
                    intent.putExtra("id",obj.getId());
                    ctx.startActivity(intent);

                }else {
                    Intent intent=new Intent(ctx,DetailsActivity.class);
                    intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());
                    ctx.startActivity(intent);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name,epiName;
        public View lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            epiName=v.findViewById(R.id.tv_epi);
        }

    }

}