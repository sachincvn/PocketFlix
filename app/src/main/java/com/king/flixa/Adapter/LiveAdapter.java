package com.king.flixa.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.king.flixa.Model.LiveModel;
import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {

    private List<LiveModel> liveModel;
    private Context context;


    public LiveAdapter(List<LiveModel> liveModel, Context context) {
        this.liveModel = liveModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.live_items,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(liveModel.get(position).getTitle());

        Glide.with(context)
                .load(liveModel.get(position).getImage())
                .transform(new RoundedCorners(20))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = liveModel.get(position).getTitle();
                String Description = liveModel.get(position).getDescription();
                String Image = liveModel.get(position).getImage();
                String streamUrl = liveModel.get(position).getLiveUrl();
                String type = liveModel.get(position).getType();
                String referer = liveModel.get(position).getReferer();
                String origin = liveModel.get(position).getOrigin();
                String cookie = liveModel.get(position).getCookie();
                String useragent = liveModel.get(position).getUseragent();
                String license= liveModel.get(position).getLicense();
                String drmScheme = liveModel.get(position).getDrmscheme()+"";

                if (type.equals("web")){
                    Toast.makeText(context, "ExpSprts", Toast.LENGTH_SHORT).show();
                }
                else if (type.equals("ads")){
                    Intent intent;
                    Uri uri = Uri.parse(streamUrl);
                    intent = new Intent(Intent.ACTION_VIEW,uri);
                    view.getContext().startActivity(intent);
                }
                else{

                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("stream-url", streamUrl);
                    intent.putExtra("license",license);
                    intent.putExtra("title",Title);
                    intent.putExtra("user-agent", useragent);
                    intent.putExtra("origin",origin);
                    intent.putExtra("referer", referer);
                    intent.putExtra("cookie",cookie);
                    intent.putExtra("drmScheme",drmScheme);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_view);
            this.textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
