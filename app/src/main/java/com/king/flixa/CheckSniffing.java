package com.king.flixa;

import android.app.Activity;
import android.content.pm.PackageManager;

public class CheckSniffing extends Activity {

    String hello = "false";

    public void isSniffingPresent(){
        if (available("com.example.helloexo")) { hello = "true"; }
        else if (available("com.minhui.networkcapture")){hello = "true"; }
        else if (available("com.guoshi.httpcanary")){hello = "true"; }
        else if (available("com.gmail.heagoo.apkeditor.parser")){hello = "true"; }
        else if (available("com.applisto.appcloner.premium")){hello = "true"; }
        else if (available("com.bajingan.bangsat")){hello = "true";}
        else {
            hello = "false";
        }
    }


    public boolean available(String name) {
        boolean available = true;
        try {
            getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            available = false;
        }
        return available;

    }
}
