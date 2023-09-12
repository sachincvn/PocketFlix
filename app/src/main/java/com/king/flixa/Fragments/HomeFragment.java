package com.king.flixa.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.king.flixa.Adapter.CategoryAdapter;
import com.king.flixa.Adapter.EventAdapter;
import com.king.flixa.Adapter.LiveAdapter;
import com.king.flixa.Adapter.SliderAdapter;
import com.king.flixa.Model.CategoryModel;
import com.king.flixa.Model.EventsModel;
import com.king.flixa.Model.LiveModel;
import com.king.flixa.Model.SliderModel;
import com.king.flixa.OpMore.OpActivity;
import com.king.flixa.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import render.animations.Bounce;
import render.animations.Render;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // Banner Slider
    RequestQueue queue;
//    String URL = "https://mocki.io/v1/09e49b0d-dea7-406d-9581-96d61d68a4f5";
//    String URLTest = "https://api.mxplayer.in/v1/web/live/channels?device-density=2&userid=cf842be9-d897-4261-aa55-6ecb37f602a4&platform=com.mxplay.mobile&content-languages=hi,en&kids-mode-enabled=false";

    String sliderApi;
    String eventsApi;
    String sportsApi;
    String liveTvApi;
    String sonyLivApi;
    String hotSportsApi;
    String mxLiveApi;

    String msgHome;
    private TextView homeMsg;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout mainLayout;
    private CardView catMore,liveMore,eventMore;

    TabLayout viewPagerIndicator;

    private ViewPager bannerSlider;
    private SliderAdapter sliderAdapter;
    private List<SliderModel> sliderModelList;
    private int currentPage = 0;
    private Timer timer;
    final private long DELAY_TIME = 3000;
    final private long PERIOD_TIME = 3000;



    //Category RecyclerView

    private  RecyclerView catRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelList;
    LinearLayoutManager HorizontalLayout;

    //Events RecyclerView
    private  RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private List<EventsModel> eventModelList;
    LinearLayoutManager eventHorizontalLayout;

    //Live RecyclerView
    private  RecyclerView liveRecyclerView;
    private LiveAdapter liveAdapter;
    private List<LiveModel> liveModelList;
    LinearLayoutManager liveHorizontalLayout;

    Dialog dialog;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout=root.findViewById(R.id.refreshLayout);
        progressBar = root.findViewById(R.id.spin_kit);
        mainLayout = root.findViewById(R.id.mainRoot);

        catMore = root.findViewById(R.id.catMore);
        liveMore = root.findViewById(R.id.liveMore);
        eventMore = root.findViewById(R.id.eventMore);

        homeMsg = root.findViewById(R.id.homeMsg);
        Render render = new Render(getActivity());
        render.setAnimation(Bounce.InLeft(homeMsg));
        render.start();

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prograss_bar_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        // bannerSlider
        viewPagerIndicator = root.findViewById(R.id.view_pager_indicator);
        bannerSlider = root.findViewById(R.id.bannerSlider);
        sliderModelList = new ArrayList<SliderModel>();



        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state==ViewPager.SCROLL_STATE_IDLE){
                    pageLooper();
                }
            }
        };
        bannerSlider.addOnPageChangeListener(onPageChangeListener);
        bannerSlideShow();

        bannerSlider.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pageLooper();
                stopSlideShow();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    bannerSlideShow();
                }
                return false;
            }
        });
        //bannerSLider


        //Recyclerview Category

        catRecyclerView = root.findViewById(R.id.catRecyclerView);
        HorizontalLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        catRecyclerView.setItemAnimator(new DefaultItemAnimator());

        catRecyclerView.setLayoutManager(HorizontalLayout);
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryModelList,getActivity());
        catRecyclerView.setAdapter(categoryAdapter);

        //Recyclerview Category


        //Recyclerview Events
        eventRecyclerView = root.findViewById(R.id.eventRecyclerView);
        eventModelList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventModelList,getActivity());
        //GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL, false);
        eventHorizontalLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        eventHorizontalLayout.setReverseLayout(false);
        eventHorizontalLayout.setStackFromEnd(false);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());

        eventRecyclerView.setLayoutManager(eventHorizontalLayout);
        eventRecyclerView.setAdapter(eventAdapter);

        //Recyclerview Events


        //Recyclerview Live
        liveRecyclerView = root.findViewById(R.id.liveRecyclerView);
        liveModelList = new ArrayList<>();
        liveAdapter = new LiveAdapter(liveModelList,getActivity());
        liveHorizontalLayout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        liveRecyclerView.setItemAnimator(new DefaultItemAnimator());

        liveRecyclerView.setLayoutManager(liveHorizontalLayout);
        liveRecyclerView.setAdapter(liveAdapter);


        Content content = new Content();
        content.execute();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                swipeRefreshLayout.setRefreshing(true);
                mainLayout.setVisibility(View.GONE);
                sliderModelList.clear();
                liveModelList.clear();
                categoryModelList.clear();
                eventModelList.clear();
                Content content = new Content();
                content.execute();
            }
        });

       catMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "No More Items", Toast.LENGTH_SHORT).show();
            }
        });
        eventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), OpActivity.class);
                i.putExtra("api",eventsApi);
                getActivity().startActivity(i);
            }
        });
        liveMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), OpActivity.class);
                i.putExtra("api",sportsApi);
                getActivity().startActivity(i);
            }
        });


        return root;
    }

    private class Content extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            dialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
            mainLayout.setVisibility(View.VISIBLE);
            //getSliderItems();
            getCatItems();
            //getLiveItems();

            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "https://blog-tech-ka.blogspot.com/2022/04/owlflix1.2.html";
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("span.expl");
                int size = data.size();

                eventsApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("events");
                sportsApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("sports");
                liveTvApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("livetv");
                sliderApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("slider");
                sonyLivApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("sonyliv");
                hotSportsApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("hot");
                mxLiveApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("mx");

                msgHome = data.select("h4.gridminfotitle")
                        .select("msg")
                        .attr("text");
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

//
//    private void getSliderItems() {
//        homeMsg.setText(msgHome);
//        queue = Volley.newRequestQueue(getActivity());
//        StringRequest request = new StringRequest(Request.Method.GET, sliderApi, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
////                Log.i("Hello World : ",response.toString());
//                try {
//                    JSONObject object=new JSONObject(response);
//                    JSONArray array=object.getJSONArray("slider");
//                    for(int i=0;i<array.length();i++) {
//                        JSONObject object1=array.getJSONObject(i);
//                        String Title = object1.getString("title");
//                        String Description = object1.getString("description");
//                        String Image = object1.getString("image");
//                        String streamUrl = object1.getString("liveUrl");
//                        String type = object1.getString("type");
//                        String referer = object1.getString("referer");
//                        String origin = object1.getString("origin");
//                        String cookie = object1.getString("cookie");
//                        String useragent = object1.getString("useragent");
//                        String license= object1.getString("license");
//                        sliderModelList.add(new SliderModel(Image,Title,Description,streamUrl,type,referer,origin,cookie,useragent,license));
//                        sliderAdapter = new SliderAdapter(sliderModelList);
//                        bannerSlider.setAdapter(sliderAdapter);
//                        bannerSlider.setClipToPadding(false);
//                        bannerSlider.setPageMargin(80);
//                        bannerSlider.setCurrentItem(0);
//                        viewPagerIndicator.setupWithViewPager(bannerSlider, true);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("error",error.toString());
//            }
//        });
//        queue.add(request);
//    }

    //BannerSlider loop
    private void pageLooper(){
        if (currentPage==sliderModelList.size()) {
            currentPage = 0;
            bannerSlider.setCurrentItem(currentPage, false);
        }
    }

    private void bannerSlideShow(){
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage>=sliderModelList.size()){
                    currentPage=0;
                }
                bannerSlider.setCurrentItem(currentPage++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DELAY_TIME,PERIOD_TIME);
    }

    private void stopSlideShow(){
        timer.cancel();
    }

    //BannerSlider loop



    @SuppressLint("NotifyDataSetChanged")
    private void getCatItems(){
            CategoryModel item1 = new CategoryModel("https://etimg.etb2bimg.com/photo/74594723.cms","HIGHLIGHTS",hotSportsApi,"hotstar");
            CategoryModel item2 = new CategoryModel("https://www.firesticktricks.com/wp-content/uploads/2021/04/aos-tv-firestick.png","LIVE TV",liveTvApi,"false");
            CategoryModel item3 = new CategoryModel("https://www.bizasialive.com/wp-content/uploads/2020/05/899ec721-sonylivnew001.jpg","SONY LIVE",sonyLivApi,"false");
            CategoryModel item4 = new CategoryModel("https://ia601005.us.archive.org/18/items/mx_player_v1.13.2_com.mxtech.videoplayer.ad/Mx-Player-Official-Logo.png","MX  LIVE",mxLiveApi,"mx");
            categoryModelList.add(item1);
            categoryModelList.add(item2);
            categoryModelList.add(item3);
            categoryModelList.add(item4);
        categoryAdapter.notifyDataSetChanged();
    }
//
//    private void getLiveItems(){
//            queue = Volley.newRequestQueue(getActivity());
//            StringRequest request = new StringRequest(Request.Method.GET, sportsApi, new Response.Listener<String>() {
//                @SuppressLint("NotifyDataSetChanged")
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject object=new JSONObject(response);
//                        JSONArray array=object.getJSONArray("live");
//                        for(int i=0;i<array.length();i++) {
//                            JSONObject object1=array.getJSONObject(i);
//                            String Title = object1.getString("title");
//                            String Description = object1.getString("description");
//                            String Image = object1.getString("image");
//                            String streamUrl = object1.getString("liveUrl");
//                            String type = object1.getString("type");
//                            String referer = object1.getString("referer");
//                            String origin = object1.getString("origin");
//                            String cookie = object1.getString("cookie");
//                            String useragent = object1.getString("useragent");
//                            String license= object1.getString("license");
//                            LiveModel items = new LiveModel(Image,Title,Description,streamUrl,type,referer,origin,cookie,useragent,license);
//                            liveModelList.add(items);
//                        }
//                        liveAdapter.notifyDataSetChanged();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("error",error.toString());
//                }
//            });
//            queue.add(request);
//        }

}