package com.king.flixa;

import static maes.tech.intentanim.CustomIntent.customType;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.king.flixa.Utils.InternetDialog;
import com.king.flixa.Utils.Utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import de.hdodenhof.circleimageview.CircleImageView;
import render.animations.Flip;
import render.animations.Render;

public class SplashActivity extends BaseActivity {
    String checkupdate;
    String Telegram;
    String UpdateLink;
    String updatepatch;
    String sharelink;
    String loginKey;
    private static int SPLASH_SCREEN_TIME_OUT=1500;
    LinearLayout server,update;
    Button telelink,updatelink;
    TextView updatedec;
    String WelcomeTExt;
    TextView load;
    String playertitle;
    Boolean connected = false;
    ProgressBar SplashProgress;
    TextView SplashTitle;
    CircleImageView SplashIcon;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        update=findViewById(R.id.update);
        server=findViewById(R.id.server);
        telelink=findViewById(R.id.telelink);
        updatelink=findViewById(R.id.updatelinks);
        updatedec=findViewById(R.id.updatepatchs);
        load=findViewById(R.id.load);
        SplashProgress=findViewById(R.id.splashProgress);
        SplashTitle=findViewById(R.id.splashTitle);
        SplashIcon=findViewById(R.id.appIcon);
        Render render = new Render(SplashActivity.this);
        render.setAnimation(Flip.InY(SplashTitle));
        render.start();

        try {
            Content content = new Content();
            content.execute();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private class Content extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onCancelled(Void unused) {
            super.onCancelled(unused);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(checkupdate.equals("yes")){
                server.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
            }
            else{
                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {
                        if (loginKey!=null && loginKey.equals(getLoginKey())){
                            Intent i = new Intent(SplashActivity.this,
                                    MainActivity.class);
                            i.putExtra("welcometext",WelcomeTExt);
                            i.putExtra("sharelink",sharelink);
                            i.putExtra("PlyrTitle",playertitle);
                            i.putExtra("loginKey",loginKey);
                            startActivity(i);
                            customType(SplashActivity.this,"fadein-to-fadeout");
                            finish();
                        }
                        else {
                            Intent i = new Intent(SplashActivity.this,
                                    LoginActivity.class);
                            i.putExtra("welcometext",WelcomeTExt);
                            i.putExtra("sharelink",sharelink);
                            i.putExtra("PlyrTitle",playertitle);
                            i.putExtra("loginKey",loginKey);
                            startActivity(i);
                            customType(SplashActivity.this,"fadein-to-fadeout");
                            finish();
                        }

                    }
                },SPLASH_SCREEN_TIME_OUT);
            }
            updatedec.setText(updatepatch);
            telelink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(Telegram); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            updatelink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(UpdateLink); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

        }
        @Override
        public Void doInBackground(Void... voids) {

            try {
                String url = "https://blog-tech-ka.blogspot.com/2021/10/update.html";

                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("span.expl");
                checkupdate = data.select("h4.gridminfotitle")
                        .select("update")
                        .attr("v1.12");
                Telegram = data.select("h4.gridminfotitle")
                        .select("telegram")
                        .attr("src");
                UpdateLink = data.select("h4.gridminfotitle")
                        .select("updatelink")
                        .attr("src");
                updatepatch = data.select("h4.gridminfotitle")
                        .select("patch")
                        .attr("src");
                WelcomeTExt = data.select("h4.gridminfotitle")
                        .select("welcome")
                        .attr("src");
                sharelink = data.select("h4.gridminfotitle")
                        .select("share")
                        .attr("src");
                playertitle = data.select("h4.gridminfotitle")
                        .select("player")
                        .attr("src");

                loginKey = data.select("h4.gridminfotitle")
                        .select("loginkey")
                        .attr("key");

            } catch (IOException e) {
                Uri uri = Uri.parse("https://t.me/owl_flix");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                //Toast.makeText(SplashActivity.this, "Join Telegram For New Update", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }
    }

}
