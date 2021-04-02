package com.nila.spareroomapp.di;

import com.nila.spareroomapp.model.UpcomingEventService;
import com.nila.spareroomapp.viewmodel.ListUpcomingViewModel;

import dagger.Component;

@Component(modules = {ApiModule.class})
public interface ApiComponent {
    void inject (UpcomingEventService service);
    void inject (ListUpcomingViewModel listViewModel);
}
