package com.king.flixa.Model.Response;

import com.king.flixa.Model.EventsModel;
import com.king.flixa.Model.LiveModel;

import java.util.ArrayList;

public class LiveChannelsResponse {

    public ArrayList<LiveModel> live;

    public ArrayList<LiveModel> getLive() {
        return live;
    }

    public void setLive(ArrayList<LiveModel> live) {
        this.live = live;
    }
}
