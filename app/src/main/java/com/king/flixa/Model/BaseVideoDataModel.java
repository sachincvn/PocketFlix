package com.king.flixa.Model;

public class BaseVideoDataModel {

    public String id;
    public String category;
    public String title;
    public String description;
    public String image;
    public String type;
    public String liveUrl;
    public String referer;
    public String origin;
    public String cookie;
    public String useragent;
    public String license;
    public int drmscheme;

    public BaseVideoDataModel(String id, String category, String title, String description, String image, String type, String liveUrl, String referer, String origin, String cookie, String useragent, String license, int drmscheme) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.description = description;
        this.image = image;
        this.type = type;
        this.liveUrl = liveUrl;
        this.referer = referer;
        this.origin = origin;
        this.cookie = cookie;
        this.useragent = useragent;
        this.license = license;
        this.drmscheme = drmscheme;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUseragent() {
        return useragent;
    }

    public void setUseragent(String useragent) {
        this.useragent = useragent;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getDrmscheme() {
        return drmscheme;
    }

    public void setDrmscheme(int drmscheme) {
        this.drmscheme = drmscheme;
    }
}
