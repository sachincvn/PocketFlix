package com.king.flixa.OpMore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.king.flixa.M3UPlaylistParser.GenericModel;
import com.king.flixa.M3UPlaylistParser.M3U8Parser;
import com.king.flixa.Model.LiveModel;
import com.king.flixa.Model.Response.LiveChannelsResponse;
import com.king.flixa.Network.ApiClient;
import com.king.flixa.Network.IApiServices;
import com.king.flixa.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpActivity extends AppCompatActivity {
    private OpAdapter opAdapter;
    private List<LiveModel> opModels;
    LinearLayoutManager liveHorizontalLayout;
    String apiUrl = "api";
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_op);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        swipeRefreshLayout=this.findViewById(R.id.refreshLayout);
        RecyclerView mxRecyclerView = findViewById(R.id.liveRecyclerView);
        opModels = new ArrayList<LiveModel>();
        opAdapter = new OpAdapter(opModels,this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        mxRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mxRecyclerView.setLayoutManager(layoutManager);
        mxRecyclerView.setAdapter(opAdapter);
        Intent intent=getIntent();
        apiUrl = intent.getStringExtra(apiUrl);
        progressBar = findViewById(R.id.spin_kit);
        getData();
        //hotStarMovies();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                opModels.clear();
                opAdapter.notifyDataSetChanged();
                getData();
            }
        });

    }


    private  void parseM3u(String data){
        try {
            M3U8Parser m3U8Parser = new M3U8Parser();
            List<GenericModel>  m3uList = m3U8Parser.parse(data);
            String a = "";
            for (GenericModel genericModel: m3uList) {
                LiveModel liveModel = new LiveModel(genericModel.getId(),
                        "1",genericModel.getTitle(),"",genericModel.getImageUrl(),"",genericModel.getUrl(),"",""
                        ,"","",genericModel.getLicence(),genericModel.getDrmScheme());
                opModels.add(liveModel);
            }
            opAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void hotStarMovies() {
        try {
            IApiServices apiServices =  ApiClient.getRetrofitInstance("https://google.com/").create(IApiServices.class);
            Call<ResponseBody> apiCall = apiServices.getStringData("https://shz.al/4zZG");
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        ResponseBody responseBody = response.body();
                        String m3udata = responseBody.string();
                        parseM3u(m3udata);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }



    private void  getData() {
        IApiServices apiServices =  ApiClient.getRetrofitInstance("https://google.com/").create(IApiServices.class);
        Call<LiveChannelsResponse> apiCall = apiServices.getLiveChannels(apiUrl);
        apiCall.enqueue(new Callback<LiveChannelsResponse>() {
            @Override
            public void onResponse(Call<LiveChannelsResponse> call, retrofit2.Response<LiveChannelsResponse> response) {
                LiveChannelsResponse responseData = response.body();
                if (responseData!=null && responseData.live!=null){
                    opModels.addAll(responseData.live);
                    opAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<LiveChannelsResponse> call, Throwable t) {
            }
        });
    }
}