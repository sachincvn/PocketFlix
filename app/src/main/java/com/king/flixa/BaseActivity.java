package com.king.flixa;

import static com.king.flixa.Utils.Utilities.isSnifffing;

import static maes.tech.intentanim.CustomIntent.customType;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.king.flixa.Utils.Utilities;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        if (isSnifffing(this)) { goodBye(); }
        Utilities.checkInternetConnection(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isSnifffing(this)) { goodBye(); }
        Utilities.checkInternetConnection(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSnifffing(this)) { goodBye(); }
        Utilities.checkInternetConnection(this);
    }
    protected void goodBye() {
        Intent i = new Intent(this, SplashActivity.class);
        startActivity(i);
        Toast.makeText(this, R.string.waste_tools, Toast.LENGTH_SHORT).show();
        finish();
    }

    protected String getLoginKey(){
        SharedPreferences sh = getSharedPreferences("SharedPrefData", MODE_PRIVATE);
        String key = sh.getString("loginKey", "");
        if (key!=null){
            return  key;
        }
        return  null;
    }

    protected void isUserLoggedIn() {
        String loginKey = getIntent().getStringExtra("loginKey");
        if (loginKey==null && !loginKey.equals(getLoginKey())){
            Intent i = new Intent(this,
                    LoginActivity.class);
            startActivity(i);
            customType(this,"fadein-to-fadeout");
            finish();
        }
    }
}
