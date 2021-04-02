package com.nila.spareroomapp.di;

import com.google.gson.GsonBuilder;
import com.nila.spareroomapp.model.UpcomingEventApi;
import com.nila.spareroomapp.model.UpcomingEventService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    private static final String BASE_URL = "https://api.jsonbin.io";

    @Provides
    public UpcomingEventApi provideUpcomingEventApi(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(UpcomingEventApi.class);
    }

    @Provides
    public UpcomingEventService provideUpcomingEventService(){
        return UpcomingEventService.getInstance();
    }

}
