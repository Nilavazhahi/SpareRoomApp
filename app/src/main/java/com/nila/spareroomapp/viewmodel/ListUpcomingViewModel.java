package com.nila.spareroomapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nila.spareroomapp.di.DaggerApiComponent;
import com.nila.spareroomapp.model.UpcomingEventService;
import com.nila.spareroomapp.model.UpcomingModel;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListUpcomingViewModel extends ViewModel {

    public MutableLiveData<List<UpcomingModel>> upcomingEvents = new MutableLiveData<List<UpcomingModel>>();
    public MutableLiveData<Boolean> loadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    @Inject
    public UpcomingEventService upcomingEventService;

    public CompositeDisposable disposable = new CompositeDisposable();

    public ListUpcomingViewModel(){
        super();
        DaggerApiComponent.create().inject(this);
    }


    public void refresh(){
        fetchUpcomingEvents();
    }

    private void fetchUpcomingEvents() {

        loading.setValue(true);
        disposable.add(
                upcomingEventService.getUpcomingEvents()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<UpcomingModel>>() {
                            @Override
                            public void onSuccess(List<UpcomingModel> upcomingModels) {

                                Iterator<UpcomingModel> iter = upcomingModels.iterator();
                                while (iter.hasNext()) {
                                    UpcomingModel upcomingModel = iter.next();
                                    if (upcomingModel.getImage_url()==null || upcomingModel.getStart_time()==null || upcomingModel.getEnd_time()==null)
                                        iter.remove();

                                }

                                upcomingEvents.setValue(upcomingModels);
                                loading.setValue(false);
                                loadError.setValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                loading.setValue(false);
                                loadError.setValue(true);
                                e.printStackTrace();

                            }
                        })
        );
    }

}
