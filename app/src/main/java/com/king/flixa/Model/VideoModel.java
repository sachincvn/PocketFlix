package com.king.flixa.Model;

public class VideoModel {
    public String streamUrl;
    public String cookieValue;
    public String refererValue;
    public  String originValue;
    public  String userAgent;
    public  String drmLicenceValue;
    public  int drmScheme;

    public VideoModel(String streamUrl, String cookieValue, String refererValue, String originValue, String userAgent, String drmLicenceValue, int drmScheme) {
        this.streamUrl = streamUrl;
        this.cookieValue = cookieValue;
        this.refererValue = refererValue;
        this.originValue = originValue;
        this.userAgent = userAgent;
        this.drmLicenceValue = drmLicenceValue;
        this.drmScheme = drmScheme;
    }
}
