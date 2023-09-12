package com.king.flixa.M3UPlaylistParser;

import com.king.flixa.M3UPlaylistParser.Util.RegexUtil;
import com.king.flixa.M3UPlaylistParser.Util.URLUtil;

import java.util.ArrayList;
import java.util.List;

public class M3U8Parser {

    private static final String REGEX_EXTRACT_NAME = "(?<=NAME=\").*?(?=\")";
    private static final String REGEX_EXTRACT_NAME_LAST_AND_R = "(?<=,).*?(?=\r)";
    private static final String REGEX_EXTRACT_NAME_TVG_NAME = "(?<=tvg-name=\").*?(?=\")";
    private static final String REGEX_EXTRACT_RESOLUTION_AND_VIRGO = "(?<=RESOLUTION=).*?(?=,)";
    private static final String REGEX_EXTRACT_GROUP_TITLE_AND_VIRGO = "(?<=group-title=).*";
    private static final String REGEX_EXTRACT_LOGO_TVG_LOGO = "(?<=tvg-logo=\").*?(?=\")";
    private static final String REGEX_EXTRACT_LICENSE_KEY = "(?<=license_key=).*";
    private static final String REGEX_EXTRACT_LICENSE_TYPE = "(?<=license_type=).*";
    private static final String NEW_LINE = "\n";


    public List<GenericModel> parse(String m3u8, String defaultImage) {
        return parseM3U8(m3u8, defaultImage);
    }

    public List<GenericModel> parse(String m3u8) {
        return parseM3U8(m3u8, null);
    }


    private List<GenericModel> parseM3U8(String m3u8, String defaultImage) {
        List<GenericModel> channels = new ArrayList<>();
        String title = null;
        String imageUrl = null;
        String licence = null;
        int drmScheme = 0;

        String[] lines = m3u8.split(NEW_LINE);

        for (String line : lines) {
            if (title != null && !line.isEmpty() && URLUtil.isValidURL(line.trim())) {
                channels.add(new GenericModel(title, imageUrl, defaultImage, line.trim(), licence, drmScheme));
                title = imageUrl = licence = null;
                drmScheme = 0;
            } else {
                if (!line.isEmpty()) {
                    title = checkTitle(line);
                    imageUrl = checkLogo(line);
                    if (licence==null){
                        licence = checkLicense(line);
                    }
                    if (drmScheme==0){
                        drmScheme = checkDrmType(line);
                    }
                }
            }
        }

        return channels;
    }


    private String checkTitle(String line) {
        if (RegexUtil.checkRegex(line, REGEX_EXTRACT_NAME) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_NAME);
        } else if (RegexUtil.checkRegex(line, REGEX_EXTRACT_RESOLUTION_AND_VIRGO) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_RESOLUTION_AND_VIRGO);
        } else if (RegexUtil.checkRegex(line, REGEX_EXTRACT_NAME_LAST_AND_R) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_NAME_LAST_AND_R);
        } else if (RegexUtil.checkRegex(line, REGEX_EXTRACT_NAME_TVG_NAME) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_NAME_TVG_NAME);
        } else if (RegexUtil.checkRegex(line, REGEX_EXTRACT_GROUP_TITLE_AND_VIRGO) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_GROUP_TITLE_AND_VIRGO);
        } else {
            return "Name Not Find";
        }
    }

    private String checkLogo(String line) {
        if (RegexUtil.checkRegex(line, REGEX_EXTRACT_LOGO_TVG_LOGO) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_LOGO_TVG_LOGO);
        } else {
            return null;
        }
    }
    public String checkLicense(String line) {
        if (RegexUtil.checkRegex(line, REGEX_EXTRACT_LICENSE_KEY) != null) {
            return RegexUtil.checkRegex(line, REGEX_EXTRACT_LICENSE_KEY);
        } else {
            return null;
        }
    }
    public int checkDrmType(String line){
        if (RegexUtil.checkRegex(line, REGEX_EXTRACT_LICENSE_TYPE) != null) {
            String drmScheme =  RegexUtil.checkRegex(line, REGEX_EXTRACT_LICENSE_TYPE);
            if (drmScheme.equals("org.w3.clearkey")){
                return 2;
            }
            else if (drmScheme.equals("com.widevine.alpha")){
                return 1;
            }
        }
        return 0;
    }
}