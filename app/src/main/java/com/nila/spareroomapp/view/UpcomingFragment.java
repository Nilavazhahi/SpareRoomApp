package com.nila.spareroomapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nila.spareroomapp.databinding.FragmentUpcomingBinding;
import com.nila.spareroomapp.model.DateList;
import com.nila.spareroomapp.model.DetailList;
import com.nila.spareroomapp.model.UpcomingList;
import com.nila.spareroomapp.model.UpcomingModel;
import com.nila.spareroomapp.viewmodel.ListUpcomingViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class UpcomingFragment extends Fragment {

    private Context context;

    private ListUpcomingViewModel listViewModel;
    private UpcomingEventListAdapter upcomingEventListAdapter = new UpcomingEventListAdapter(new ArrayList<>());
    List<UpcomingList> upcomingConsolidatedList = new ArrayList<>();

    private FragmentUpcomingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();

        loadData();

        binding.errorRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        binding.offlineRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        binding.upcomingEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= "07466887291";
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:"+phone ));
                context.startActivity(intent);
            }
        });
    }

    private void loadData() {
        if(Connectivity.isConnected(context)) {

            listViewModel = ViewModelProviders.of(this).get(ListUpcomingViewModel.class);
            listViewModel.refresh();

            binding.upcomingEventList.setLayoutManager(new LinearLayoutManager(context));
            binding.upcomingEventList.setAdapter(upcomingEventListAdapter);

            observerViewModel();
        }else{
            binding.errorLayout.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.GONE);
            binding.upcomingEventList.setVisibility(View.GONE);
            binding.noConnectionLayout.setVisibility(View.VISIBLE);

        }

    }

    private void observerViewModel() {
        listViewModel.upcomingEvents.observe(this, new Observer<List<UpcomingModel>>() {
            @Override
            public void onChanged(List<UpcomingModel> upcomingModels) {
                if(upcomingModels !=null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

                    Iterator<UpcomingModel> iter = upcomingModels.iterator();
                    while (iter.hasNext()) {
                        UpcomingModel upcomingModel = iter.next();
                        try {
                            Date date = sdf.parse(upcomingModel.getStart_time());
                            if (System.currentTimeMillis() > date.getTime()) {
                                iter.remove();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                    Collections.sort(upcomingModels, new Comparator<UpcomingModel>() {
                        public int compare(UpcomingModel um1, UpcomingModel um2) {
                            if (um1.getStart_time() != null || um2.getStart_time() != null)
                                return um1.getStart_time().compareTo(um2.getStart_time());
                            else
                                return 0;
                        }
                    });

                    TreeMap<String, List<UpcomingModel>> groupedTreeMap = groupDataIntoHashMap(upcomingModels);


                    for (String date : groupedTreeMap.keySet()) {
                        DateList dateItem = new DateList();
                        dateItem.setDate(date);
                        upcomingConsolidatedList.add(dateItem);


                        for (UpcomingModel upcomingModel : groupedTreeMap.get(date)) {
                            DetailList detailListItem = new DetailList();
                            detailListItem.setUpcomingModel(upcomingModel);
                            upcomingConsolidatedList.add(detailListItem);
                        }
                    }

                    binding.upcomingEventList.setVisibility(View.VISIBLE);
                    upcomingEventListAdapter.updateUpcomingEvents(upcomingConsolidatedList);
                }else{
                    binding.emptyLayout.setVisibility(View.VISIBLE);
                    binding.loadingLayout.setVisibility(View.VISIBLE);
                    binding.upcomingEventList.setVisibility(View.GONE);
                }
            }
        });

        listViewModel.loadError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if(isError !=null){
                    binding.errorLayout.setVisibility(isError ? View.VISIBLE : View.GONE);
                }
                if(isError){
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.loadingLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        listViewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null){
                    binding.loadingLayout.setVisibility( isLoading ? View.VISIBLE : View.GONE);
                }

                if(isLoading){
                    binding.emptyLayout.setVisibility(View.GONE);
                    binding.upcomingEventList.setVisibility(View.GONE);
                    binding.errorLayout.setVisibility(View.GONE);
                    binding.noConnectionLayout.setVisibility(View.GONE);

                }
            }
        });
    }

    private TreeMap<String, List<UpcomingModel>> groupDataIntoHashMap(List<UpcomingModel> listOfUpcomingModel) {

        TreeMap<String, List<UpcomingModel>> groupedTreeMap = new TreeMap<>();

        for (UpcomingModel upcomingModel : listOfUpcomingModel) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM");

            Date startDate = null;
            try {
                startDate = dateFormat.parse(upcomingModel.getStart_time());

                String date = dateFormatter.format(startDate);
                if (date.endsWith("01") && !date.endsWith("11"))
                    dateFormatter = new SimpleDateFormat("MMMM");

                else if (date.endsWith("02") && !date.endsWith("12"))
                    dateFormatter = new SimpleDateFormat("MMMM");

                else if (date.endsWith("03") && !date.endsWith("13"))
                    dateFormatter = new SimpleDateFormat("MMMM");

                else
                    dateFormatter = new SimpleDateFormat("MMMM");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String hashMapKey = dateFormatter.format(startDate);

            if (groupedTreeMap.containsKey(hashMapKey)) {
                groupedTreeMap.get(hashMapKey).add(upcomingModel);
            } else {
                List<UpcomingModel> list = new ArrayList<>();
                list.add(upcomingModel);
                groupedTreeMap.put(hashMapKey, list);
            }
        }

        return groupedTreeMap;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}