package com.king.flixa.Utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.widget.Toast;

import com.king.flixa.Model.VideoModel;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static boolean isApplicationInstalled(Context context, String name) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
            return true; // App is installed
        } catch (PackageManager.NameNotFoundException e) {
            return false; // App is not installed
        }
    }
    public static boolean isSnifffing(Context context){
        if (isApplicationInstalled(context,decodeBase64("Y29tLmd1b3NoaS5odHRwY2FuYXJ5LnByZW1pdW0="))) { return true; }
        else if (isApplicationInstalled(context,decodeBase64("Y29tLm1pbmh1aS5uZXR3b3JrY2FwdHVyZQ=="))){return true; }
        else if (isApplicationInstalled(context,decodeBase64("YmluLm10LnBsdXM="))){return true; }
        else if (isApplicationInstalled(context,decodeBase64("Y29tLmd1b3NoaS5odHRwY2FuYXJ5"))){return true; }
        else if (isApplicationInstalled(context,decodeBase64("Y29tLmdtYWlsLmhlYWdvby5hcGtlZGl0b3IucGFyc2Vy"))){return true; }
        else if (isApplicationInstalled(context,decodeBase64("Y29tLmFwcGxpc3RvLmFwcGNsb25lci5wcmVtaXVt"))){return true;}
        else if (isApplicationInstalled(context,decodeBase64("Y29tLmJhamluZ2FuLmJhbmdzYXQ="))){return true;}
        else {
            return false;
        }
    }

    private static String decodeBase64(String s){
        byte[] data = Base64.decode(s, Base64.DEFAULT);
        String decodedSt = new String(data, StandardCharsets.UTF_8);
        return decodedSt;
    }

    public static void checkInternetConnection(Context context){
        new InternetDialog(context).getInternetStatus();
    }

    public static VideoModel getUrlParameters(String url){
        String[] parts = url.split("\\|");
        String mainUrl = parts[0].trim();
        String userAgentValue = null;
        String refererValue = null;
        String cookieValue = null;
        String originValue = null;
        String userAgentRegex = "User-Agent=([^&]+)";
        String refererRegex = "Referer=([^&]+)";
        String originRegex = "Origin=([^&]+)";
        String cookieRegex = "Cookie=([^&]+)";

        Pattern userAgentPattern = Pattern.compile(userAgentRegex);
        Pattern refererPattern = Pattern.compile(refererRegex);
        Pattern originPattern = Pattern.compile(originRegex);
        Pattern cookiePattern = Pattern.compile(cookieRegex);

        Matcher userAgentMatcher = userAgentPattern.matcher(url);
        Matcher refererMatcher = refererPattern.matcher(url);
        Matcher originMatcher = originPattern.matcher(url);
        Matcher cookieMatcher = cookiePattern.matcher(url);

        // Find User-Agent value
        if (userAgentMatcher.find()) {
            userAgentValue = userAgentMatcher.group(1);
        }

        // Find Referer value
        if (refererMatcher.find()) {
            refererValue = refererMatcher.group(1);
        }

        // Find Cookie value
        if (cookieMatcher.find()) {
            cookieValue = cookieMatcher.group(1);
        }
        if (originMatcher.find()) {
            originValue = originMatcher.group(1);
        }

        return  new VideoModel(mainUrl,cookieValue,refererValue,originValue,userAgentValue,"",0);
    }
}
