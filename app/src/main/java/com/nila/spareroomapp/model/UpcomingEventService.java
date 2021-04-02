package com.nila.spareroomapp.model;

import com.nila.spareroomapp.di.DaggerApiComponent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class UpcomingEventService {

    private static final String BASE_URL = "https://api.jsonbin.io";

    private static UpcomingEventService instance;

    @Inject
    public UpcomingEventApi api;


    private UpcomingEventService(){
        DaggerApiComponent.create().inject(this);
    }

    public static UpcomingEventService getInstance(){
        if(instance == null){
            instance = new UpcomingEventService();
        }
        return instance;
    }

    public Single<List<UpcomingModel>> getUpcomingEvents(){
        return api.getUpcomingEvents();
    }
}
