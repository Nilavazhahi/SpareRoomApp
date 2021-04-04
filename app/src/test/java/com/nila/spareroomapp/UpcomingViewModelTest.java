package com.nila.spareroomapp;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.nila.spareroomapp.model.UpcomingEventService;
import com.nila.spareroomapp.model.UpcomingModel;
import com.nila.spareroomapp.viewmodel.ListUpcomingViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

public class UpcomingViewModelTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    UpcomingEventService upcomingEventService;

    @InjectMocks
    ListUpcomingViewModel listViewModel = new ListUpcomingViewModel();

    private Single<List<UpcomingModel>> testSingle;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUpcomingEventSuccess(){

        UpcomingModel upcomingModel = new UpcomingModel("image_url", "phone_number", "cost", "location", "venue", "start_time", "end_time");
        ArrayList<UpcomingModel> upcomingList = new ArrayList<>();
        upcomingList.add(upcomingModel);

        testSingle = Single.just(upcomingList);
        Mockito.when(upcomingEventService.getUpcomingEvents()).thenReturn(testSingle);
        listViewModel.refresh();

        Assert.assertEquals(1, listViewModel.upcomingEvents.getValue().size());
        Assert.assertEquals(false, listViewModel.loadError.getValue());
        Assert.assertEquals(false, listViewModel.loading.getValue());
    }

    @Test
    public void getUpcomingEventFailure(){

        testSingle = Single.error(new Throwable());

        Mockito.when(upcomingEventService.getUpcomingEvents()).thenReturn(testSingle);

        listViewModel.refresh();

        Assert.assertEquals(true,listViewModel.loadError.getValue());
        Assert.assertEquals(false, listViewModel.loading.getValue());
    }

    @Before
    public void setupRxSchedulers(){
        Scheduler immediate =new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(runnable -> {runnable.run(); },true);
            }
        };
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler->immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler->immediate);
    }

}
