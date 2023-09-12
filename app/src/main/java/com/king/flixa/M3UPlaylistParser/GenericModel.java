package com.king.flixa.M3UPlaylistParser;

import java.util.UUID;

public class GenericModel {
    private final String id;
    private final String title;
    private final String imageUrl;
    private final String url;
    private final String licence;
    private final int drmScheme;

    public GenericModel(String title, String imageUrl, String defaultImage, String url, String licence, int drmScheme) {
        this.title = title;
        this.licence = licence;
        this.drmScheme = drmScheme;
        if (imageUrl != null && !imageUrl.isEmpty()) {
            this.imageUrl = imageUrl;
        } else {
            this.imageUrl = defaultImage;
        }
        this.url = url;
        this.id = UUID.randomUUID().toString();
    }

    public String getLicence() {
        return licence;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }
    public int getDrmScheme() {
        return drmScheme;
    }
}