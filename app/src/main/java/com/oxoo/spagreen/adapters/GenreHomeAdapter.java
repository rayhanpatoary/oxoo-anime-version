package com.oxoo.spagreen.adapters;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oxoo.spagreen.ItemMovieActivity;
import com.oxoo.spagreen.R;
import com.oxoo.spagreen.models.CommonModels;
import com.oxoo.spagreen.models.GenreModel;

import java.util.ArrayList;
import java.util.List;

public class GenreHomeAdapter extends RecyclerView.Adapter<GenreHomeAdapter.OriginalViewHolder> {

    private List<GenreModel> items = new ArrayList<>();
    private List<CommonModels> listData = new ArrayList<>();
    private Context ctx;


    public GenreHomeAdapter(Context context, List<GenreModel> items) {
        this.items = items;
        ctx = context;
    }


    @Override
    public GenreHomeAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GenreHomeAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_genre_home, parent, false);
        vh = new GenreHomeAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenreHomeAdapter.OriginalViewHolder holder, final int position) {

        final GenreModel obj = items.get(position);
        holder.name.setText(obj.getName());

        HomePageAdapter adapter=new HomePageAdapter(ctx,obj.getList());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView.setAdapter(adapter);

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ctx, ItemMovieActivity.class);
                intent.putExtra("id",obj.getId());
                intent.putExtra("title",obj.getName());
                intent.putExtra("type","genre");
                ctx.startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        RecyclerView recyclerView;
        Button btnMore;


        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.tv_name);
            recyclerView=v.findViewById(R.id.recyclerView);
            btnMore=v.findViewById(R.id.btn_more);
        }
    }




}
