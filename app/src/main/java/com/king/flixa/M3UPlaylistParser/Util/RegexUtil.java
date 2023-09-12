package com.king.flixa.M3UPlaylistParser.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class RegexUtil {

    private RegexUtil() {
        // DO NOTHING
    }

    public static String checkRegex(String toCheck, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(toCheck);
        if (m.find()) {
            return m.group();
        } else {
            return null;
        }
    }

}