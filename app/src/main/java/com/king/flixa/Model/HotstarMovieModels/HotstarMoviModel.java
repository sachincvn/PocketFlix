package com.king.flixa.Model.HotstarMovieModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HotstarMoviModel {
    public Body body;
    public String statusCode;
    public int statusCodeValue;

    public HotstarMoviModel(Body body, String statusCode, int statusCodeValue) {
        this.body = body;
        this.statusCode = statusCode;
        this.statusCodeValue = statusCodeValue;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCodeValue() {
        return statusCodeValue;
    }

    public void setStatusCodeValue(int statusCodeValue) {
        this.statusCodeValue = statusCodeValue;
    }
}

class AutoplayObj{
    public String contentId;
    public String playbackType;
    public String orientation;
}

class Collection{
    public String name;
}

class DEFAULT{
    public String b;
    public String t;
    public String v;
    public String h;
    public String i;
    public String m;
}

class GenreObj{
    public int id;
    public String name;
    public String engName;
    public String detailUrl;
}

class ImageSets{
    @SerializedName("DEFAULT")
    public DEFAULT dEFAULT;
}

class ImageSetV2{
    public String publicUri;
    public String imageType;
    public String theme;
    public double height;
    public double width;
}

class LangObj{
    public boolean hide;
    public int id;
    public String name;
    public String iso3code;
    public String detailUrl;
    public String displayName;
}

class OriginalLanguage{
    public boolean hide;
    public int id;
    public String name;
    public String iso3code;
    public String displayName;
}


