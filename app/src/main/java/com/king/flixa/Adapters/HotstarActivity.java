package com.king.flixa.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.king.flixa.Adapters.Api.MainInterface;
import com.king.flixa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HotstarActivity extends AppCompatActivity {


    public static final String apiUri = "apiUri";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private final ArrayList<MainData> dataArrayList = new ArrayList<>();
    private MainAdapter adapter;
    int page =0,limit =20;

    private final Boolean fetcData = false;
    private ProgressBar mainprogressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotstar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        NestedScrollView nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        mainprogressBar = findViewById(R.id.spin_kit);

        adapter = new MainAdapter(HotstarActivity.this,dataArrayList);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);



        getData(page,limit);



        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    page=page+20;
                    progressBar.setVisibility(View.VISIBLE);
                    getData(page,limit);
                }
            }
        });
    }
    private void getData(int page, int limit) {
        Intent intent = getIntent();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.hotstar.com/o/v1/tray/e/"+intent.getStringExtra(apiUri)+"/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface mainInterface = retrofit.create(MainInterface.class);


        Call<String> call = mainInterface.STRING_CALL(page,limit);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful() && response.body()!=null){
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    try {

                        JSONObject jsonObject = new JSONObject(response.body()).getJSONObject("body").getJSONObject("results");
                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                        praseResult(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void praseResult(JSONArray jsonArray) {


        for (int i=0;i<jsonArray.length();i++){
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                MainData data = new MainData();

                JSONObject getThumUrl = new JSONObject(object.getString("images"));

                data.setImage(getThumUrl .getString("h"));
                data.setName(object.getString("title"));
                data.setContentId(object.getString("contentId"));

                dataArrayList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new MainAdapter(HotstarActivity.this,dataArrayList);
            recyclerView.setAdapter(adapter);
            mainprogressBar.setVisibility(View.GONE);
        }
    }

}