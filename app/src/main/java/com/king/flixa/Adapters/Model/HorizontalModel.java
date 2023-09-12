package com.king.flixa.Adapters.Model;

public class HorizontalModel {
    String name,description,contentId,image;

    public HorizontalModel(String name, String description, String contentId, String image) {
        this.name = name;
        this.description = description;
        this.contentId = contentId;
        this.image = image;
    }

    public HorizontalModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}