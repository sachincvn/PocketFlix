package com.king.flixa.FilmyHit;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.MxPlayer.MxModel;
import com.king.flixa.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FilmyhitAdapter extends RecyclerView.Adapter<FilmyhitAdapter.ViewHolder> {

    private List<MxModel> liveModel;
    private Context context;
    Dialog dialog;
    TextView progressTitle;
    public FilmyhitAdapter(List<MxModel> liveModel, Context context) {
        this.liveModel = liveModel;
        this.context = context;
    }

    @NonNull
    @Override
    public FilmyhitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row_main,parent,false);

        return new FilmyhitAdapter.ViewHolder(view);
    }

    private class GenerateFilmyHitLinks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String movieUrl = null;
            if (strings.length>1){
                try {
                    Document document = Jsoup.connect(url).get();
                    Elements flimyhitsMovieUrl = document.select("#my-video source" );
                    movieUrl = flimyhitsMovieUrl.attr("src");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                Document document = Jsoup.connect(url).get();
                Elements flimyhitsMovieUrl = document.select("div.loaer_detai a");
                movieUrl = flimyhitsMovieUrl.attr("href");
            } catch (IOException e) {
                e.printStackTrace();
                }
            }

            return movieUrl;
        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
        }


    }

        @Override
    public void onBindViewHolder(@NonNull FilmyhitAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.prograss_bar_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                progressTitle =  dialog.findViewById(R.id.progressTitle);
                progressTitle.setText("Generating Link");

                String videoUrl = null;
                GenerateFilmyHitLinks getMovieUrl = new GenerateFilmyHitLinks();
                getMovieUrl.execute(liveModel.get(position).getUrl());
                try {
                    String url = getMovieUrl.get();
                    GenerateFilmyHitLinks getVideoUrl = new GenerateFilmyHitLinks();
                    getVideoUrl.execute(url,url);
                    videoUrl = getVideoUrl.get();
                } catch (ExecutionException e) {
                    dialog.dismiss();
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    dialog.dismiss();
                    throw new RuntimeException(e);
                }
                if (videoUrl!=null){
                    dialog.dismiss();
                    Intent intent;
                    intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("title",liveModel.get(position).getTitle());
                    intent.putExtra("user-agent", "ExoPlayer2");
                    intent.putExtra("origin","");
                    intent.putExtra("referer", "");
                    intent.putExtra("cookie","");
                    intent.putExtra("stream-url",videoUrl);
                   context.startActivity(intent);
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
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
