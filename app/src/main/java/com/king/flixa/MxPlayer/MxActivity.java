package com.king.flixa.MxPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.king.flixa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MxActivity extends AppCompatActivity {

    private MxAdapter mxAdapter;
    private List<MxModel> mxModels;
    RequestQueue queue;
    String apiUrl = "api";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mx);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        RecyclerView mxRecyclerView = findViewById(R.id.liveRecyclerView);
        mxModels = new ArrayList<MxModel>();
        mxAdapter = new MxAdapter(mxModels,this);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        mxRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mxRecyclerView.setLayoutManager(layoutManager);
        mxRecyclerView.setAdapter(mxAdapter);
        Intent intent=getIntent();
        apiUrl = intent.getStringExtra(apiUrl);
        progressBar = findViewById(R.id.spin_kit);
        getData();
    }
    private void getData(){
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("channels");
                    for(int i=0;i<array.length();i++) {
                        JSONObject object1=array.getJSONObject(i);
                        String Title = object1.getString("title");
                        JSONArray myImage = object1.getJSONArray("imageInfo");
                        String Image = myImage.getJSONObject(0).getString("url");

                        String streamUrl =object1.getJSONObject("stream").getJSONObject("mxplay")
                                .getJSONObject("hls").getString("main");
                        MxModel items = new MxModel("https://qqcdnpictest.mxplay.com/"+Image,Title,streamUrl);
                        mxModels.add(items);
                        progressBar.setVisibility(View.GONE);
                    }
                    mxAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }

}