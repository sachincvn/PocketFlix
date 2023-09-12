package com.king.flixa.Model.Response;

import com.king.flixa.Model.EventsModel;

import java.util.ArrayList;
import java.util.List;

public class EventResponse{
    public ArrayList<EventsModel> live;

    public ArrayList<EventsModel> getLive() {
        return live;
    }

    public void setLive(ArrayList<EventsModel> live) {
        this.live = live;
    }
}


