package com.nila.spareroomapp.model;

public class UpcomingModel {

    private String image_url =null;
    private String phone_number;
    private String cost;
    private String location;
    private String venue;
    private String start_time;
    private String end_time;

    public UpcomingModel(String image_url, String phone_number, String cost, String location, String venue, String start_time, String end_time) {
        this.image_url = image_url;
        this.phone_number = phone_number;
        this.cost = cost;
        this.location = location;
        this.venue = venue;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getPhone_number() {
        return phone_number;
    }


    public String getCost() {
        return cost;
    }


    public String getLocation() {
        return location;
    }


    public String getVenue() {
        return venue;
    }


    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

}
