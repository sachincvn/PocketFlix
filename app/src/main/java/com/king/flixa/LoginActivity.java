package com.king.flixa;

import static maes.tech.intentanim.CustomIntent.customType;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity  extends AppCompatActivity {

    EditText loginKeyTextField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginKeyTextField = findViewById(R.id.editText2);
    }

    public void slideUp(View view){
        String loginKey = getIntent().getStringExtra("loginKey");
        String inputKey = loginKeyTextField.getText().toString();
        if (inputKey!=null && !inputKey.isEmpty() && inputKey.trim().equals(loginKey)){
            setLoginKey(loginKey);
            Intent i = new Intent(LoginActivity.this,
                    MainActivity.class);
            i.putExtra("welcometext",getIntent().getStringExtra("welcometext"));
            i.putExtra("sharelink",getIntent().getStringExtra("sharelink"));
            i.putExtra("PlyrTitle",getIntent().getStringExtra("PlyrTitle"));
            i.putExtra("loginKey",loginKey);
            startActivity(i);
            customType(LoginActivity.this,"fadein-to-fadeout");
            finish();
        }
        else {
            Toast.makeText(this,"Please enter a valid login key",Toast.LENGTH_SHORT).show();
        }
    }

    private void setLoginKey(String loginKey){
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefData", MODE_PRIVATE);
        SharedPreferences.Editor mySharedPrefData = sharedPreferences.edit();
        mySharedPrefData.putString("loginKey", loginKey);
        mySharedPrefData.apply();
    }
}
