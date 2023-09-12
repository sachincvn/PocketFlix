package com.king.flixa.FilmyHit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.king.flixa.MainActivity;
import com.king.flixa.MxPlayer.MxAdapter;
import com.king.flixa.MxPlayer.MxModel;
import com.king.flixa.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class FilmyhitActivity extends AppCompatActivity {
    private FilmyhitAdapter mxAdapter;
    private List<MxModel> mxModels;
    private ProgressBar progressBar;

    private int currentPage = 0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmyhit);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        RecyclerView mxRecyclerView = findViewById(R.id.liveRecyclerView);
        mxModels = new ArrayList<MxModel>();
        mxAdapter = new FilmyhitAdapter(mxModels,this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        mxRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mxRecyclerView.setLayoutManager(layoutManager);
        mxRecyclerView.setAdapter(mxAdapter);

        progressBar = findViewById(R.id.spin_kit);

        mxRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) { // Adjust the totalItemCount threshold as needed
                        isLoading = true;
                        currentPage++;
                        progressBar.setVisibility(View.VISIBLE);
                        new FilmyhitContent().execute(currentPage);
                    }
                }
            }
        });

        new FilmyhitContent().execute(currentPage);
    }

    private class FilmyhitContent extends AsyncTask<Integer, Void, List<MxModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<MxModel> result) {
            progressBar.setVisibility(View.GONE);
            if (result != null && !result.isEmpty()) {
                mxModels.addAll(result);
                setMovies();
            } else {
                Toast.makeText(FilmyhitActivity.this, "No more data available.", Toast.LENGTH_SHORT).show();
            }
            isLoading = false;
            super.onPostExecute(result);
        }

        @Override
        protected List<MxModel> doInBackground(Integer... pageNumbers) {
            int pageNumber = pageNumbers[0];
            try {
                String url = "https://filmy-hit.lat/movies/All-Hindi-Movies/date/" + pageNumber + ".html";
                Document filmyhitDoc = Jsoup.connect(url).get();
                Elements data = filmyhitDoc.select("div.mov-col2");
                Elements filmyhitsMovies = data.select("ul.nw-vdo li a.video");
                List<MxModel> pageMovies = new ArrayList<>();

                for (Element filmyhitsMovie : filmyhitsMovies) {
                    String title = filmyhitsMovie.select("a.video span h4").attr("title").toString();
                    String image = filmyhitsMovie.select("a.video span img").attr("src");
                    String videoUrl = filmyhitsMovie.select("a.video").attr("href");
                    MxModel items = new MxModel(image, title, videoUrl);
                    pageMovies.add(items);
                }

                return pageMovies;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void setMovies() {
       mxAdapter.notifyDataSetChanged();
       progressBar.setVisibility(View.GONE);
    }

}
