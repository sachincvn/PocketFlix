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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.king.flixa.Model.EventsModel;
import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.R;
import com.king.flixa.webview.WebActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventsModel> eventModel;
    private Context context;


    public EventAdapter(List<EventsModel> eventModel, Context context) {
        this.eventModel = eventModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.exp_items,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(eventModel.get(position).getTitle().toUpperCase());

        Glide.with(context)
                .load(eventModel.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .transform(new RoundedCorners(20))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = eventModel.get(position).getTitle();
                String Description = eventModel.get(position).getDescription();
                String Image = eventModel.get(position).getImage();
                String streamUrl = eventModel.get(position).getLiveUrl();
                String type = eventModel.get(position).getType();
                String referer = eventModel.get(position).getReferer();
                String origin = eventModel.get(position).getOrigin();
                String cookie = eventModel.get(position).getCookie();
                String useragent = eventModel.get(position).getUseragent();
                String license= eventModel.get(position).getLicense();
                String drmScheme = eventModel.get(position).getDrmscheme()+"";


                if (type.equals("web")){
                    Intent intent;
                    intent = new Intent(view.getContext(), WebActivity.class);
                    intent.putExtra("webUrl", streamUrl);
                    view.getContext().startActivity(intent);
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
        return eventModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.bannnerImage);
            this.textView = (TextView) itemView.findViewById(R.id.bannerTitle);
        }
    }
}
