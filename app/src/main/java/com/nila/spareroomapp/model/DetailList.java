package com.nila.spareroomapp.model;

public class DetailList extends UpcomingList {

    private UpcomingModel upcomingModel;

    public UpcomingModel getUpcomingModel() {
        return upcomingModel;
    }

    public void setUpcomingModel(UpcomingModel upcomingModel) {
        this.upcomingModel = upcomingModel;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }

}
