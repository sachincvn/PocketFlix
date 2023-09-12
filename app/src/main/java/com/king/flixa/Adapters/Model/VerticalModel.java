package com.king.flixa.Adapters.Model;

import java.util.ArrayList;

/**
 * Created by Vatsal on 09-06-2020.
 */

public class VerticalModel {

    String title;
    String apiUri;
    ArrayList<HorizontalModel> arrayList;

    public VerticalModel(String title, String apiUri, ArrayList<HorizontalModel> arrayList) {
        this.title = title;
        this.apiUri = apiUri;
        this.arrayList = arrayList;
    }

    public VerticalModel() {

    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<HorizontalModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<HorizontalModel> arrayList) {
        this.arrayList = arrayList;
    }
}