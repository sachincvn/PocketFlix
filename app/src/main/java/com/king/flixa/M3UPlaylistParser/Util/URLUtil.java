package com.king.flixa.M3UPlaylistParser.Util;

import java.net.URL;
import java.util.regex.Pattern;

public class URLUtil {

    private URLUtil() {
        // DO NOTHING
    }

//    public static boolean isValidURL(String urlString) {
//        try {
//            new URL(urlString).toURI();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public static boolean isValidURL(String urlString) {
        String urlRegex = "^(https?|ftp)://[a-zA-Z0-9.-]+(\\.[a-zA-Z]{2,4})(:[0-9]+)?([/?].*)?$";
        return Pattern.matches(urlRegex, urlString);
    }
}