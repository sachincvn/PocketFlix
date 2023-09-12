package com.king.flixa.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.king.flixa.Adapters.Model.HorizontalModel;
import com.king.flixa.Adapters.Model.VerticalModel;
import com.king.flixa.Model.HotstarMovieModels.Assets;
import com.king.flixa.Model.HotstarMovieModels.HotstarMoviModel;
import com.king.flixa.Model.HotstarMovieModels.Item;
import com.king.flixa.Network.ApiClient;
import com.king.flixa.Network.IApiServices;
import com.king.flixa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class HotSports extends AppCompatActivity {

    String user_identity;
    RecyclerView verticalRecyclerView;
    VerticalRecyclerViewAdapter adapter;
    ArrayList<VerticalModel> arrayListVertical =new ArrayList<>();

    String apiUrl = "api";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotsports);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        verticalRecyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        verticalRecyclerView.setHasFixedSize(true);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        arrayListVertical=new ArrayList<>();
        adapter=new VerticalRecyclerViewAdapter(this,arrayListVertical);
        verticalRecyclerView.setAdapter(adapter);
        Intent intent=getIntent();
        apiUrl = intent.getStringExtra(apiUrl);

        progressBar = findViewById(R.id.spin_kit);
        hotStarMovies();
        //setData();
    }

    private void hotStarMovies() {
       try {
           IApiServices apiServices =  ApiClient.getRetrofitInstance("https://api.hotstar.com/").create(IApiServices.class);
           retrofit2.Call<HotstarMoviModel> apiCall = apiServices.getHotstarMovies("https://api.hotstar.com/o/v1/page/1958?offset=0&size=20&tao=0&tas=100");
           apiCall.enqueue(new retrofit2.Callback<HotstarMoviModel>() {
               @Override
               public void onResponse(retrofit2.Call<HotstarMoviModel> call, retrofit2.Response<HotstarMoviModel> response) {
                   HotstarMoviModel hotstarMoviModel = response.body();
                   if (hotstarMoviModel.body.results != null &&
                           hotstarMoviModel.body.results.trays!=null &&
                           hotstarMoviModel.body.results.trays.items != null)
                   {
                       for (Item item: hotstarMoviModel.body.results.trays.items) {
                           ArrayList<HorizontalModel> arraylist=new ArrayList<>();
                           if (item.assets != null && item.assets.items != null){
                               VerticalModel verticalModel=new VerticalModel();
                               verticalModel.setTitle(item.title);
                               verticalModel.setApiUri(item.uri);
                               arrayListVertical.add(verticalModel);

                               for (Item items:item.assets.items) {
                                   HorizontalModel horizontalModel=new HorizontalModel();
                                   horizontalModel.setDescription(items.detail);
                                   horizontalModel.setName(items.title);
                                   horizontalModel.setContentId(items.contentId+"");
                                   String imageUrl = items.images.h;
                                   horizontalModel.setImage("https://img1.hotstarext.com/image/upload/f_auto,t_web_hs_2_5x/"+imageUrl);
                                   arraylist.add(horizontalModel);
                               }
                               verticalModel.setArrayList(arraylist);
                           }
                       }
                       adapter.notifyDataSetChanged();
                       progressBar.setVisibility(View.GONE);
                   }

               }

               @Override
               public void onFailure(retrofit2.Call<HotstarMoviModel> call, Throwable t) {

               }
           });

       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
    private void setData() {
        OkHttpClient client = new OkHttpClient();
        JSONObject postData = new JSONObject();
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        String url = "https://assignmentmanagerd.000webhostapp.com/hauth.php";
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonBody ="{\"device_ids\":[{\"id\":\"c37bde49-5a21-4db5-b667-905266196227\",\"type\":\"device_id\"}],\"device_meta\":{\"network_operator\":\"4g - 10 - 150\",\"os_name\":\"Android\",\"os_version\":\"11\"}}";
                okhttp3.Request post = new okhttp3.Request.Builder()
                        .url("https://api.hotstar.com/um/v3/users")
                        .header("hotstarauth",response.toString())
                        .header("x-hs-platform","mweb")
                        .header("X-HS-Request-Id","0d3bdf36-abf6-4e2b-8126-f53b31e450df")
                        .header("X-Country-Code","IN")
                        .header("Content-Type","application/json")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonBody))
                        .build();

                client.newCall(post).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        if (response.isSuccessful()) {
                            final String myResponse = response.body().string();
                            HotSports.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject obj = new JSONObject(myResponse);
                                        user_identity = obj.getString("user_identity").toString();
                                        Log.i("user_identity",user_identity);


                                        RequestQueue queue = Volley.newRequestQueue(HotSports.this);
                                        String url = apiUrl;
                                        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                                                new Response.Listener<String>()
                                                {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void onResponse(String response) {
                                                        // response
//                                                        Log.d("Response", response);
                                                        try {
                                                            JSONObject object=new JSONObject(response).getJSONObject("body")
                                                                    .getJSONObject("results").getJSONObject("trays");
                                                            JSONArray array=object.getJSONArray("items");

                                                            for(int i=2;i<array.length();i++) {
                                                                JSONObject object1=array.getJSONObject(i);
                                                                String Title = object1.getString("title");
                                                                String uri = object1.getString("uri");
                                                                String uriId = object1.getString("id");

                                                                VerticalModel verticalModel=new VerticalModel();
                                                                verticalModel.setTitle(Title);
                                                                verticalModel.setApiUri(uriId);
                                                                ArrayList<HorizontalModel> arraylist=new ArrayList<>();

                                                                RequestQueue queue = Volley.newRequestQueue(HotSports.this);
                                                                String url = uri;
                                                                StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                                                                        new Response.Listener<String>()
                                                                        {
                                                                            @Override
                                                                            public void onResponse(String response) {
                                                                                try {
                                                                                    JSONObject object=new JSONObject(response).getJSONObject("body").getJSONObject("results").getJSONObject("assets");
                                                                                    JSONArray array=object.getJSONArray("items");

                                                                                    for(int j=0;j<array.length();j++) {
                                                                                        JSONObject object1 = array.getJSONObject(j);
                                                                                        String Title = object1.getString("title");
                                                                                        String contentId = object1.getString("contentId");

                                                                                        JSONObject getThumb = new JSONObject(object1.getString("images").toString());
                                                                                        String imageUrl = getThumb.getString("h").toString();

                                                                                        HorizontalModel horizontalModel=new HorizontalModel();
                                                                                        horizontalModel.setDescription("Description"+ j);
                                                                                        horizontalModel.setName(Title);
                                                                                        horizontalModel.setContentId(contentId);
                                                                                        horizontalModel.setImage("https://img1.hotstarext.com/image/upload/f_auto,t_web_hs_2_5x/"+imageUrl);
                                                                                        arraylist.add(horizontalModel);
                                                                                        progressBar.setVisibility(View.GONE);

                                                                                    }
                                                                                    adapter.notifyDataSetChanged();
                                                                                } catch (JSONException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        },
                                                                        new Response.ErrorListener()
                                                                        {
                                                                            @Override
                                                                            public void onErrorResponse(VolleyError error) {
                                                                                // TODO Auto-generated method stub
                                                                                Log.d("ERROR","error => "+error.toString());
                                                                            }
                                                                        }
                                                                ) {
                                                                    @Override
                                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                                        Map<String, String>  params = new HashMap<String, String>();
                                                                        params.put("x-hs-usertoken", user_identity);
                                                                        params.put("x-country-code", "IN");
                                                                        params.put("x-platform-code", "PCTV");

                                                                        return params;
                                                                    }
                                                                };
                                                                queue.add(getRequest);
                                                                verticalModel.setArrayList(arraylist);
                                                                arrayListVertical.add(verticalModel);
                                                            }
                                                            adapter.notifyDataSetChanged();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener()
                                                {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // TODO Auto-generated method stub
                                                        Log.d("ERROR","error => "+error.toString());
                                                    }
                                                }
                                        ) {
                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {
                                                Map<String, String>  params = new HashMap<String, String>();
                                                params.put("x-hs-usertoken", user_identity);
                                                params.put("x-country-code", "IN");
                                                params.put("x-platform-code", "PCTV");

                                                return params;
                                            }
                                        };
                                        queue.add(getRequest);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        else{
                            Log.e("user_identity", response.toString());
                        }
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Error :" , error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);

    }


}