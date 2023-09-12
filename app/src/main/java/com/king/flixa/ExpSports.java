package com.king.flixa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.king.flixa.Adapter.LiveAdapter;
import com.king.flixa.Model.LiveModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExpSports extends AppCompatActivity {

    private RecyclerView liveRecyclerView;
    private LiveAdapter liveAdapter;
    private List<LiveModel> liveModelList;
    LinearLayoutManager liveHorizontalLayout;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expsports);

        liveRecyclerView = findViewById(R.id.liveRecyclerView);
        liveModelList = new ArrayList<>();
        liveAdapter = new LiveAdapter(liveModelList,this);
        liveRecyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        liveRecyclerView.setLayoutManager(layoutManager);
        liveRecyclerView.setAdapter(liveAdapter);

        getLiveItems();

    }
    private void getLiveItems(){
        String sportsApi = "https://raw.githubusercontent.com/mrvc962/opsports/main/event19.json";
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, sportsApi, new Response.Listener<String>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("live");
                    for(int i=0;i<array.length();i++) {
                        JSONObject object1=array.getJSONObject(i);
                        String Title = object1.getString("title");
                        String Description = object1.getString("description");
                        String Image = object1.getString("image");
                        String streamUrl = object1.getString("liveUrl");
                        String type = object1.getString("type");
                        String referer = object1.getString("referer");
                        String origin = object1.getString("origin");
                        String cookie = object1.getString("cookie");
                        String useragent = object1.getString("useragent");
                        String license= object1.getString("license");
//                        LiveModel items = new LiveModel(Image,Title,Description,streamUrl,type,referer,origin,cookie,useragent,license);
//                        liveModelList.add(items);
                    }
                    liveAdapter.notifyDataSetChanged();
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