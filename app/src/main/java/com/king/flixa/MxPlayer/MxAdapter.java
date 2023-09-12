package com.king.flixa.MxPlayer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MxAdapter extends RecyclerView.Adapter<MxAdapter.ViewHolder> {

    private List<MxModel> liveModel;
    private Context context;


    public MxAdapter(List<MxModel> liveModel, Context context) {
        this.liveModel = liveModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row_main,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(liveModel.get(position).getTitle());

        Glide.with(context)
                .load(liveModel.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .transform(new RoundedCorners(20))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("title",liveModel.get(position).getTitle());
                intent.putExtra("user-agent", "ExoPlayer2");
                intent.putExtra("origin","");
                intent.putExtra("referer", "");
                intent.putExtra("cookie","");
                intent.putExtra("stream-url",liveModel.get(position).getUrl().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_view);
            this.textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
