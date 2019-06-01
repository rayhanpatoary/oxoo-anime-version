package com.anime.spagreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anime.spagreen.AnimeDetailsActivity;
import com.anime.spagreen.AnimePlayerActivity;
import com.anime.spagreen.DetailsActivity;
import com.anime.spagreen.R;
import com.anime.spagreen.models.CommonModels;
import com.balysv.materialripple.MaterialRippleLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AnimeRelatedAdapter extends RecyclerView.Adapter<AnimeRelatedAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;


    public AnimeRelatedAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public AnimeRelatedAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AnimeRelatedAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_anime_related, parent, false);
        vh = new AnimeRelatedAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AnimeRelatedAdapter.OriginalViewHolder holder, final int position) {

        final CommonModels obj = items.get(position);
        holder.name.setText(obj.getTitle());
        Picasso.get().load(obj.getImageUrl()).into(holder.image);

        holder.aired.setText("Aired: "+obj.getAired());
        holder.time.setText("Runtime: "+obj.getRunTime());

        holder.lyt_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (obj.getVideoType().equals("tvseries")){
                    Intent intent=new Intent(ctx, AnimeDetailsActivity.class);
                    intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());

                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }

                else if (obj.getVideoType().equals("epi")){
                    Intent intent=new Intent(ctx, AnimePlayerActivity.class);
                    //intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());

                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }else {
                    Intent intent=new Intent(ctx, DetailsActivity.class);
                    intent.putExtra("vType",obj.getVideoType());
                    intent.putExtra("id",obj.getId());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        public TextView name,aired,time;
        public MaterialRippleLayout lyt_parent;


        public OriginalViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.image);
            name = v.findViewById(R.id.name);
            lyt_parent=v.findViewById(R.id.lyt_parent);
            aired=v.findViewById(R.id.aired);
            time=v.findViewById(R.id.runtime);
        }
    }

}
