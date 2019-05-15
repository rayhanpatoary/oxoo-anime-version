package com.oxoo.spagreen.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oxoo.spagreen.R;
import com.oxoo.spagreen.models.CommentsModel;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.OriginalViewHolder> {

    private List<CommentsModel> items = new ArrayList<>();
    private Context ctx;



    public ReplyAdapter(Context context, List<CommentsModel> items) {
        this.items = items;
        ctx = context;

    }


    @Override
    public ReplyAdapter.OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReplyAdapter.OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reply, parent, false);
        vh = new ReplyAdapter.OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReplyAdapter.OriginalViewHolder holder, final int position) {

        final CommentsModel obj = items.get(position);


        holder.name.setText(obj.getName());
        holder.comment.setText(obj.getComment());

        Picasso.get().load(obj.getImage()).into(holder.imageView);




    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        private TextView name,comment;
        private View lyt_parent;
        private CircularImageView imageView;


        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            lyt_parent=v.findViewById(R.id.lyt_parent);
            imageView=v.findViewById(R.id.profile_img);
            comment=v.findViewById(R.id.comments);
        }
    }

}