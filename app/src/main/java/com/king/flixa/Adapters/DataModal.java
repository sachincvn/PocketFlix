package com.king.flixa.Adapters;

public class DataModal {

    // string variables for our name and job
    private String name;
    private String job;
    private String contentId;

    public DataModal(String name, String job, String contentId) {
        this.name = name;
        this.job = job;
        this.contentId = contentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
}
