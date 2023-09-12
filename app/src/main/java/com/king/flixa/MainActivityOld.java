package com.king.flixa;

import static com.king.flixa.Utils.Utilities.isSnifffing;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.tabs.TabLayout;
import com.king.flixa.Adapter.CategoryAdapter;
import com.king.flixa.Adapter.EventAdapter;
import com.king.flixa.Adapter.LiveAdapter;
import com.king.flixa.Adapter.SliderAdapter;
import com.king.flixa.Adapters.HotSports;
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
import com.king.flixa.webview.WebActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

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
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivityOld extends AppCompatActivity {

    //HOME

    RequestQueue queue;
    String sliderApi;
    String eventsApi;
    String sportsApi;
    String liveTvApi;
    String sonyLivApi;
    String hotSportsApi;
    String mxLiveApi;
    String jioTvApi;
    String moviesApi;
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

    private MeowBottomNavigation bnv_Main;
    Toolbar toolbar;
//    private static final String ONESIGNAL_APP_ID = "67274add-490f-4096-b1fd-abe103507430";
    AlertDialog.Builder builderr;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//        OneSignal.initWithContext(this);
//        OneSignal.setAppId(ONESIGNAL_APP_ID);


        if (isSnifffing(this)) { goodBye(); }
        else {
            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            MainActivityOld.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
           // bootomNav();
            HomeActivity();

        }

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
        dialog.show();


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


        Content content = new Content();
        content.execute();


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
                Toast.makeText(MainActivityOld.this, "No More Items", Toast.LENGTH_SHORT).show();
            }
        });
        eventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityOld.this, OpActivity.class);
                i.putExtra("api",eventsApi);
                MainActivityOld.this.startActivity(i);
            }
        });
        liveMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityOld.this, OpActivity.class);
                i.putExtra("api",sportsApi);
                MainActivityOld.this.startActivity(i);
            }
        });


    }

    private void  getLiveEvents() {
       try {
           IApiServices apiServices =  ApiClient.getRetrofitInstance("https://pastebin.com/").create(IApiServices.class);
           Call<EventResponse> apiCall = apiServices.getApiRequest(eventsApi);
           apiCall.enqueue(new Callback<EventResponse>() {
               @Override
               public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                   Toast.makeText(MainActivityOld.this,"Success ",Toast.LENGTH_SHORT);
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



    //HOME


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
                String url = "https://unknown-king-live.blogspot.com/2023/08/KingFlix.html";
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
                jioTvApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("jiotv");
                moviesApi = data.select("h4.gridminfotitle")
                        .select("api")
                        .attr("movies");


                msgHome = data.select("h4.gridminfotitle")
                        .select("msg")
                        .attr("text");
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
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

    //BannerSlider loop



    @SuppressLint("NotifyDataSetChanged")
    private void getCatItems(){
        //CategoryModel item1 = new CategoryModel("https://etimg.etb2bimg.com/photo/74594723.cms","Highlights",hotSportsApi,"hotstar");
        CategoryModel item1 = new CategoryModel("https://i.ibb.co/mJ4f1hw/Sports-Live.png","Sports",sportsApi,"false");
        CategoryModel item2 = new CategoryModel("https://www.firesticktricks.com/wp-content/uploads/2021/04/aos-tv-firestick.png","Live Tv",liveTvApi,"false");
        CategoryModel item3 = new CategoryModel("https://i.ibb.co/1TKjvjt/Jio-TV.png","Jiotv",jioTvApi,"false");
        CategoryModel item4 = new CategoryModel("https://i.ibb.co/jGhppDB/Movies.png","Movies",moviesApi,"false");

        //CategoryModel item3 = new CategoryModel("https://www.bizasialive.com/wp-content/uploads/2020/05/899ec721-sonylivnew001.jpg","SONY LIVE",sonyLivApi,"false");
        //CategoryModel item4 = new CategoryModel("https://ia601005.us.archive.org/18/items/mx_player_v1.13.2_com.mxtech.videoplayer.ad/Mx-Player-Official-Logo.png","MX  LIVE",mxLiveApi,"mx");
        categoryModelList.add(item1);
        categoryModelList.add(item2);
        categoryModelList.add(item3);
        categoryModelList.add(item4);
        categoryAdapter.notifyDataSetChanged();
    }

    //HOME

//    private void bootomNav() {
//
//        bnv_Main = findViewById(R.id.bottom_navigation);
//
//        bnv_Main.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
//        bnv_Main.add(new MeowBottomNavigation.Model(2, R.drawable.ic_movie));
//        bnv_Main.add(new MeowBottomNavigation.Model(3, R.drawable.ic_add));
//        bnv_Main.add(new MeowBottomNavigation.Model(4, R.drawable.ic_notification));
//
//
//        bnv_Main.show(1, true);
//        replace(new HomeFragment());
//        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
//            @Override
//            public Unit invoke(MeowBottomNavigation.Model model) {
//                switch (model.getId()) {
//                    case 1:
//                        replace(new HomeFragment());
//                        break;
//
//                    case 2:
//                        replace(new MoviesFragment());
//                        break;
//
//                    case 3:
//                        replace(new NetStreamFragment());
//                        break;
//
//                    case 4:
//                        replace(new DownloadFragment());
//                        break;
//
//                }
//                return null;
//            }
//        });
//    }

    private void uninstallOldApp(String oldApp) {
        builderr = new AlertDialog.Builder(this);
        builderr.setMessage("Please uninstall older version application")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:"+oldApp));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertt = builderr.create();
        alertt.setTitle("Uninstall older app");
        alertt.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nWatch Live Sports & Live TV in ExpSpots\n\nDownlod Now From The Below Links & Enjoy Live Matches\n\n";
            shareMessage = shareMessage + getIntent().getStringExtra("sharelink")+"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        }
        else if (item.getItemId() == R.id.privacy){
            Intent i = new Intent(MainActivityOld.this,
                    WebActivity.class);
            i.putExtra("webUrl","file:///android_asset/privacy.html");
            startActivity(i);
        }
        else if (item.getItemId() == R.id.terms){
            Intent i = new Intent(MainActivityOld.this,
                    WebActivity.class);
            i = new Intent(MainActivityOld.this,
                    WebActivity.class);
            i.putExtra("webUrl","file:///android_asset/terms.html");
            startActivity(i);
        }
        else if (item.getItemId() == R.id.contact){
            Uri uri = Uri.parse("https://t.me/ipl_live_links_bot"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.networkStream){
            Intent i = new Intent(MainActivityOld.this,
                    HotSports.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeDialogStatus(boolean isChecked){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();

    }

    private boolean getDialogStatus(){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }

    private void goodBye() {
        Intent i = new Intent(MainActivityOld.this,
                SplashActivity.class);
        startActivity(i);
        Toast.makeText(this, R.string.waste_tools, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isSnifffing(this)) { goodBye(); }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isSnifffing(this)) { goodBye(); }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSnifffing(this)) { goodBye(); }
    }
}