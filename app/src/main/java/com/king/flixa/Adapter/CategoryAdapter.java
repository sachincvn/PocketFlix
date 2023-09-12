package com.king.flixa.Adapter;

import static maes.tech.intentanim.CustomIntent.customType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.king.flixa.Adapters.HotSports;
import com.king.flixa.FilmyHit.FilmyhitActivity;
import com.king.flixa.Model.CategoryModel;
import com.king.flixa.MxPlayer.MxActivity;
import com.king.flixa.OpMore.OpActivity;
import com.king.flixa.R;
import com.bumptech.glide.Glide;
import com.king.flixa.SplashActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModel;
    private Context context;
    public String isott;
    public String api;


    public CategoryAdapter(List<CategoryModel> categoryModel, Context context) {
        this.categoryModel = categoryModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cat_items,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(categoryModel.get(position).getTitle());
        Glide.with(context)
                .load(categoryModel.get(position).getImage())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api = categoryModel.get(position).getApi();
                isott = categoryModel.get(position).getIsott();
                if (isott.equals("hotstar")){
                    Intent i = new Intent(context, FilmyhitActivity.class);
                    i.putExtra("api",api);
                    context.startActivity(i);
                }
                else if (isott.equals("mx")){
                    Intent i = new Intent(context, MxActivity.class);
                    i.putExtra("api",api);
                    context.startActivity(i);
                }
                else {
                    Intent i = new Intent(context, OpActivity.class);
                    i.putExtra("api",api);
                    context.startActivity(i);
                    customType(context,"fadein-to-fadeout");
                }
            }
        });
    }
    

    @Override
    public int getItemCount() {
        return categoryModel.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (CircleImageView) itemView.findViewById(R.id.cat_image);
            this.textView = (TextView) itemView.findViewById(R.id.catTitle);
        }
    }
}
