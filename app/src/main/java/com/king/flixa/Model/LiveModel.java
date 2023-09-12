package com.king.flixa.Model;

public class LiveModel    extends  BaseVideoDataModel{

    public LiveModel(String id, String category, String title, String description, String image, String type, String liveUrl, String referer, String origin, String cookie, String useragent, String license, int drmscheme) {
        super(id, category, title, description, image, type, liveUrl, referer, origin, cookie, useragent, license, drmscheme);
    }
}