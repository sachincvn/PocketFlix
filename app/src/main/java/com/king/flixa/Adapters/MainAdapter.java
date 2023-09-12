package com.king.flixa.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.king.flixa.ExpPlayer.PlayerActivity;
import com.king.flixa.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<MainData> dataArrayList;
    private Activity activity;

    String user_identity;
    String hotstar_auth;
    Dialog dialog;
    TextView progressTitle;

    public MainAdapter(Activity activity,ArrayList<MainData> dataArrayList){
        this.activity = activity;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainData data = dataArrayList.get(position);

        Glide.with(activity)
                .load("https://img1.hotstarext.com/image/upload/f_auto,t_web_m_1x/"+data.getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .transform(new RoundedCorners(20))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);


        holder.textView.setText(data.getName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contentId = data.getContentId();
                String videoTitle = data.getName();
                generateLinks(contentId,videoTitle);
            }
        });
    }

    private void generateLinks(String contentId,String videoTitle) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prograss_bar_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        progressTitle =  dialog.findViewById(R.id.progressTitle);
        progressTitle.setText("Generating Link");
        OkHttpClient client = new OkHttpClient();
        JSONObject postData = new JSONObject();
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        String url = "https://babybitch.herokuapp.com/tokengen.php";
        mRequestQueue = Volley.newRequestQueue(activity);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hotstar_auth=response.toString();
                String jsonBody ="{\"device_ids\":[{\"id\":\"c37bde49-5a21-4db5-b667-905266196227\",\"type\":\"device_id\"}],\"device_meta\":{\"network_operator\":\"4g - 10 - 150\",\"os_name\":\"Android\",\"os_version\":\"11\"}}";
                okhttp3.Request post = new okhttp3.Request.Builder()
                        .url("https://api.hotstar.com/um/v3/users")
                        .header("hotstarauth",hotstar_auth)
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
                            ((Activity)activity).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject obj = new JSONObject(myResponse);
                                        user_identity = obj.getString("user_identity").toString();
                                        Log.i("user_identity",user_identity);
                                        OkHttpClient client = new OkHttpClient();
                                        JSONObject postData = new JSONObject();

                                        String jsonBody ="{\"os_name\":\"Android\",\"os_version\":\"11\",\"app_name\":\"mweb\",\"app_version\":\"7.34.1\",\"platform\":\"Chrome\",\"platform_version\":\"98.0.4758.87\",\"client_capabilities\":{\"ads\":[\"non_ssai\"],\"audio_channel\":[\"stereo\"],\"dvr\":[\"short\"],\"package\":[\"dash\",\"hls\"],\"dynamic_range\":[\"sdr\"],\"video_codec\":[\"h264\"],\"encryption\":[\"widevine\"],\"ladder\":[\"phone\"],\"container\":[\"fmp4\"],\"resolution\":[\"fhd\",\"hd\"]},\"language\":\"eng\",\"drm_parameters\":{\"widevine_security_level\":[\"HW_SECURE_ALL\",\"HW_SECURE_DECODE\",\"HW_SECURE_CRYPTO\",\"SW_SECURE_DECODE\",\"SW_SECURE_CRYPTO\"],\"hdcp_version\":[\"HDCP_NO_DIGITAL_OUTPUT\"]},\"resolution\":\"auto\"}";
                                        okhttp3.Request post = new okhttp3.Request.Builder()
                                                .url("https://api.hotstar.com/play/v4/playback/content/"+contentId+"?device-id=5e9eb69d-c931-4a66-9330-edae3ed207ac&desired-config=audio_channel:stereo|container:fmp4|dynamic_range:sdr|encryption:widevine|ladder:phone|language:eng|package:dash|resolution:fhd|video_codec:h264")
                                                .header("x-Hs-UserToken",user_identity)
                                                .header("hotstarauth",hotstar_auth)
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
                                                    ((Activity)activity).runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String user_identity;
                                                            String playback_url;
                                                            String licence_url;
                                                            try {
                                                                JSONObject obj = new JSONObject(myResponse).getJSONObject("data");
                                                                JSONObject object = new JSONObject(String.valueOf(obj));
                                                                String data =object.getString("playback_sets");

                                                                JSONArray playback_sets = new JSONArray(data);
                                                                Log.i("Response main",playback_sets.toString());
                                                                for(int i=0;i<playback_sets.length();i++) {
                                                                    JSONObject object1 = playback_sets.getJSONObject(i);
                                                                    JSONObject object2 = new JSONObject(String.valueOf(object1));
                                                                    String tags_combination_eng = "audio_channel:stereo;audio_codec:aac;container:fmp4;dynamic_range:sdr;encryption:widevine;language:eng;package:dash;resolution:fhd;video_codec:h264";
                                                                    String tags_combination_hindi = "audio_channel:stereo;audio_codec:aac;container:fmp4;dynamic_range:sdr;encryption:widevine;language:hin;package:dash;resolution:fhd;video_codec:h264";
                                                                    if(tags_combination_hindi.equals(object2.getString("tags_combination"))){
                                                                        playback_url = object2.getString("playback_url");
                                                                        licence_url = object2.getString("licence_url");
                                                                        dialog.dismiss();
                                                                        Intent intent = new Intent(activity, PlayerActivity.class);
                                                                        intent.putExtra("stream-url", playback_url);
                                                                        intent.putExtra("license",licence_url);
                                                                        intent.putExtra("title",videoTitle);
                                                                        intent.putExtra("user-agent", "Hotstar;in.startv.hotstar/12.3.0.1118 (Android/10)");
                                                                        intent.putExtra("origin","https://www.hotstar.com");
                                                                        intent.putExtra("referer", "https://www.hotstar.com/");
                                                                        intent.putExtra("cookie","");
                                                                        activity.startActivity(intent);
                                                                        break;
                                                                    }
                                                                    else if (tags_combination_eng.equals(object2.getString("tags_combination"))){
                                                                        playback_url = object2.getString("playback_url");
                                                                        licence_url = object2.getString("licence_url");
                                                                        dialog.dismiss();
                                                                        Intent intent = new Intent(activity, PlayerActivity.class);
                                                                        intent.putExtra("stream-url", playback_url);
                                                                        intent.putExtra("license",licence_url);
                                                                        intent.putExtra("title",videoTitle);
                                                                        intent.putExtra("user-agent", "Hotstar;in.startv.hotstar/12.3.0.1118 (Android/10)");
                                                                        intent.putExtra("origin","https://www.hotstar.com");
                                                                        intent.putExtra("referer", "https://www.hotstar.com/");
                                                                        intent.putExtra("cookie","");
                                                                        activity.startActivity(intent);
                                                                        break;
                                                                    }
                                                                    else {
                                                                        Log.i("Response data","Not Found");
                                                                    }
                                                                }




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


    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);


        }
    }
}
