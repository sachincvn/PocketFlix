package com.king.flixa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.king.flixa.Adapter.CategoryAdapter;
import com.king.flixa.Adapter.EventAdapter;
import com.king.flixa.Adapter.LiveAdapter;
import com.king.flixa.Adapter.SliderAdapter;
import com.king.flixa.Model.CategoryModel;
import com.king.flixa.Model.EventsModel;
import com.king.flixa.Model.LiveModel;
import com.king.flixa.Model.Response.LiveChannelsResponse;
import com.king.flixa.Model.Response.SliderBannerResponse;
import com.king.flixa.Model.SliderModel;
import com.king.flixa.Model.Response.EventResponse;
import com.king.flixa.Network.ApiClient;
import com.king.flixa.Network.IApiServices;
import com.king.flixa.OpMore.OpActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import render.animations.Bounce;
import render.animations.Render;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends BaseActivity {

    private String url = "https://blog-tech-ka.blogspot.com/2022/04/owlflix1.2.html";
    private String sliderApi,eventsApi,sportsApi,sonyLivApi,hotSportsApi,mxLiveApi,jioTvApi, moviesApi,msgHome;
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

    private RecyclerView catRecyclerView;
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
    //HOME
    private static final String REGEX_EXTRACT_LICENSE_KEY = "(?<=license_key=).*";
    private MeowBottomNavigation bnv_Main;
    Toolbar toolbar;
    //    private static final String ONESIGNAL_APP_ID = "67274add-490f-4096-b1fd-abe103507430";
    AlertDialog.Builder builderr;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isUserLoggedIn();
        setContentView(R.layout.fragment_home);

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        MainActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        HomeActivity();

    }

    private void HomeActivity() {

        swipeRefreshLayout=this.findViewById(R.id.refreshLayout);
        progressBar = this.findViewById(R.id.spin_kit);
        mainLayout = this.findViewById(R.id.mainRoot);

        catMore = this.findViewById(R.id.catMore);
        liveMore = this.findViewById(R.id.liveMore);
        eventMore = this.findViewById(R.id.eventMore);

        homeMsg = this.findViewById(R.id.homeMsg);
        Render render = new Render(this);
        render.setAnimation(Bounce.InLeft(homeMsg));
        render.start();

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prograss_bar_dialog);
        dialog.setCanceledOnTouchOutside(false);
        //dialog.show();


        // bannerSlider
        viewPagerIndicator = this.findViewById(R.id.view_pager_indicator);
        bannerSlider = this.findViewById(R.id.bannerSlider);
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

        catRecyclerView = this.findViewById(R.id.catRecyclerView);
        HorizontalLayout = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        catRecyclerView.setItemAnimator(new DefaultItemAnimator());

        catRecyclerView.setLayoutManager(HorizontalLayout);
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryModelList,this);
        catRecyclerView.setAdapter(categoryAdapter);

        //Recyclerview Category


        //Recyclerview Events
        eventRecyclerView = this.findViewById(R.id.eventRecyclerView);
        eventModelList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventModelList,this);
        //GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL, false);
        eventHorizontalLayout = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        eventHorizontalLayout.setReverseLayout(false);
        eventHorizontalLayout.setStackFromEnd(false);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());

        eventRecyclerView.setLayoutManager(eventHorizontalLayout);
        eventRecyclerView.setAdapter(eventAdapter);

        //Recyclerview Events


        //Recyclerview Live
        liveRecyclerView = this.findViewById(R.id.liveRecyclerView);
        liveModelList = new ArrayList<>();
        liveAdapter = new LiveAdapter(liveModelList,this);
        liveHorizontalLayout = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        liveRecyclerView.setItemAnimator(new DefaultItemAnimator());

        liveRecyclerView.setLayoutManager(liveHorizontalLayout);
        liveRecyclerView.setAdapter(liveAdapter);

       try {
           Content content = new Content();
           content.execute();
       }
       catch (Exception ex){
           ex.printStackTrace();
       }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    dialog.show();
                    swipeRefreshLayout.setRefreshing(true);
                    mainLayout.setVisibility(View.GONE);
                    sliderModelList.clear();
                    liveModelList.clear();
                    categoryModelList.clear();
                    eventModelList.clear();

                    sliderAdapter.notifyDataSetChanged();
                    liveAdapter.notifyDataSetChanged();
                    eventAdapter.notifyDataSetChanged();

                    Content content = new Content();
                    content.execute();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        catMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "No More Items", Toast.LENGTH_SHORT).show();
            }
        });
        eventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OpActivity.class);
                i.putExtra("api",eventsApi);
                MainActivity.this.startActivity(i);
            }
        });
        liveMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OpActivity.class);
                i.putExtra("api",sportsApi);
                MainActivity.this.startActivity(i);
            }
        });
    }

    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            mainLayout.setVisibility(View.VISIBLE);
            getSliderItems();
            getLiveEvents();
            getLiveChannels();
            getCatItems();
            dialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document filmyhitDoc = Jsoup.connect("https://filmy-hit.lat/movies/All-Hindi-Movies/date/0.html").get();
                Elements flimyhitsMovies = filmyhitDoc.select("div.mov-col2");
                Elements fData = flimyhitsMovies.select("ul.nw-vdo li");


                Document doc = Jsoup.connect(url).get();
                Elements data = doc.select("span.expl");
                int size = data.size();


                Elements apiElements = data.select("catlist.categories api");

                for (Element apiElement : apiElements) {
                    String title = apiElement.attr("name");
                    String apiUrl = apiElement.attr("url");
                    String icon = apiElement.attr("icon");
                    String ott = apiElement.attr("ott");
                    categoryModelList.add(new CategoryModel(icon,title,apiUrl,ott));
                }

                eventsApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("events");
                sportsApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("sports");
                sliderApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("slider");

                msgHome = data.select("h4.gridminfotitle")
                        .select("msg")
                        .attr("text");

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @SuppressLint("NotifyDataSetChanged")
        private void getCatItems(){
            categoryAdapter.notifyDataSetChanged();
        }
        private void  getLiveEvents() {
            try {
                IApiServices apiServices =  ApiClient.getRetrofitInstance("https://pastebin.com/").create(IApiServices.class);
                Call<EventResponse> apiCall = apiServices.getApiRequest(eventsApi);
                apiCall.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                        EventResponse responseData = response.body();
                        if (responseData!=null && responseData.live != null){
                            eventModelList.addAll(responseData.live);
                            eventAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        private void  getLiveChannels() {
            try {
                IApiServices apiServices =  ApiClient.getRetrofitInstance("https://google.com/").create(IApiServices.class);
                Call<LiveChannelsResponse> apiCall = apiServices.getLiveChannels(sportsApi);
                apiCall.enqueue(new Callback<LiveChannelsResponse>() {
                    @Override
                    public void onResponse(Call<LiveChannelsResponse> call, retrofit2.Response<LiveChannelsResponse> response) {
                        LiveChannelsResponse responseData = response.body();
                        if (responseData!=null && responseData.live!=null){
                            liveModelList.addAll(responseData.live);
                            liveAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<LiveChannelsResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        private void  getSliderItems() {
            try {
                if (msgHome!=null && !msgHome.isEmpty()){
                    homeMsg.setText(msgHome);
                }
                else {
                    homeMsg.setText(R.string.app_name);
                }
                IApiServices apiServices =  ApiClient.getRetrofitInstance("https://google.com/").create(IApiServices.class);
                Call<SliderBannerResponse> apiCall = apiServices.getBannerItems(sliderApi);
                apiCall.enqueue(new Callback<SliderBannerResponse>() {
                    @Override
                    public void onResponse(Call<SliderBannerResponse> call, retrofit2.Response<SliderBannerResponse> response) {
                        SliderBannerResponse responseData = response.body();
                        if (responseData!=null && responseData.slider !=null){
                            sliderModelList.addAll(responseData.slider);
                            sliderAdapter = new SliderAdapter(sliderModelList);
                            bannerSlider.setAdapter(sliderAdapter);
                            bannerSlider.setClipToPadding(false);
                            bannerSlider.setPageMargin(80);
                            bannerSlider.setCurrentItem(0);
                            viewPagerIndicator.setupWithViewPager(bannerSlider, true);
                        }
                    }
                    @Override
                    public void onFailure(Call<SliderBannerResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

    }


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

}