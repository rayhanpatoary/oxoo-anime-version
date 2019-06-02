package com.anime.spagreen.adapters;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anime.spagreen.R;
import com.anime.spagreen.models.CommonModels;

import java.util.ArrayList;
import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.OriginalViewHolder> {

    private List<CommonModels> items = new ArrayList<>();
    private Context ctx;

    public EpisodeAdapter(Context context, List<CommonModels> items) {
        this.items = items;
        ctx = context;
    }
    @Override
    public EpisodeAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EpisodeAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_episode, parent, false);
        vh = new EpisodeAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EpisodeAdapter.OriginalViewHolder holder, final int position) {

        CommonModels obj = items.get(position);

        DirectorApater directorApater=new DirectorApater(ctx,obj.getListEpi());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setAdapter(directorApater);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;


        public OriginalViewHolder(View v) {
            super(v);
            recyclerView=v.findViewById(R.id.recyclerView);


        }
    }

}