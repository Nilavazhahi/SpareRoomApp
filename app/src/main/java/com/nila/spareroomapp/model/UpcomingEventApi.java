package com.nila.spareroomapp.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface UpcomingEventApi {

    @Headers("secret-key: $2b$10$76APFiNwr0YXKLX6FDCGiuks/TPFnSKkJleMY2uz1AR1EqTK9IODC\n")
    @GET("/b/605479c67ffeba41c07de021")
    Single<List<UpcomingModel>> getUpcomingEvents();
}
