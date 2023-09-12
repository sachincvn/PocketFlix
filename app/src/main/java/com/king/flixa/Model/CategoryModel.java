package com.king.flixa.Model;

public class CategoryModel {
    private String image;
    private String title;
    private String api;
    private String isott;

    public CategoryModel(String image, String title, String api, String isott) {
        this.image = image;
        this.title = title;
        this.api = api;
        this.isott = isott;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getIsott() {
        return isott;
    }

    public void setIsott(String isott) {
        this.isott = isott;
    }
}
