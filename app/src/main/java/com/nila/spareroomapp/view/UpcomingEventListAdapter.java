 package com.nila.spareroomapp.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.nila.spareroomapp.R;
import com.nila.spareroomapp.model.DateList;
import com.nila.spareroomapp.model.DetailList;
import com.nila.spareroomapp.model.UpcomingList;
import com.nila.spareroomapp.model.UpcomingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingEventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UpcomingList> upcomingEvents;
    private Context context;


    public UpcomingEventListAdapter(List<UpcomingList> upcomingEvents){
        this.upcomingEvents = upcomingEvents;
    }

    public void updateUpcomingEvents(List<UpcomingList> newUpcomingEvents){
        upcomingEvents.clear();
        upcomingEvents.addAll(newUpcomingEvents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case UpcomingList.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.upcoming_list, parent,
                        false);
                viewHolder = new GeneralViewHolder(v1);
                break;

            case UpcomingList.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.upcoming_date_list, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

       switch (holder.getItemViewType()) {

            case UpcomingList.TYPE_GENERAL:

                DetailList generalItem   = (DetailList) upcomingEvents.get(position);
                GeneralViewHolder generalViewHolder= (GeneralViewHolder) holder;
                generalViewHolder.bind(generalItem.getUpcomingModel());

                break;

            case UpcomingList.TYPE_DATE:

                DateList dateItem = (DateList) upcomingEvents.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;

                dateViewHolder.dateHeader.setText(dateItem.getDate());
                if(position>0)
                    dateViewHolder.dividerView.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return upcomingEvents.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return upcomingEvents != null ? upcomingEvents.size() : 0;
    }


    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date_header)
        TextView dateHeader;
        @BindView(R.id.divider_view)
        View dividerView;

        public DateViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

    // View holder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.event_image)
        ImageView eventImage;

        @BindView(R.id.venue)
        TextView eventVenue;

        @BindView(R.id.event_location)
        TextView eventLocation;

        @BindView(R.id.event_date)
        TextView eventDate;

        @BindView(R.id.event_time)
        TextView eventTime;

        @BindView(R.id.event_cost)
        TextView eventCost;

        @BindView(R.id.card_view)
        CardView cardView;

        public GeneralViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int callPermissionCheck = ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CALL_PHONE);

                    if (callPermissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                (Activity) v.getContext(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                0);
                    }else {
                        int pos = getLayoutPosition();
                        DetailList generalItem   = (DetailList) upcomingEvents.get(pos);
                        String phoneNo = generalItem.getUpcomingModel().getPhone_number();

                        if (phoneNo != null) {

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNo));
                            v.getContext().startActivity(intent);
                        }
                    }
                }
            });
        }

        void bind(UpcomingModel upcomingModel){

            if(upcomingModel.getImage_url()!=null && upcomingModel.getImage_url()!="" && upcomingModel.getStart_time()!=null && upcomingModel.getEnd_time()!=null
                    && upcomingModel.getStart_time()!="" && upcomingModel.getEnd_time()!="") {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM-dd");
                SimpleDateFormat startTimeFormatter = new SimpleDateFormat("h:mm");
                SimpleDateFormat endTimeFormatter = new SimpleDateFormat("h:mm aaa");


                String eventDateStr = "";
                String startTime = "";
                String endTime = "";
                Date endDate = null;
                try {
                    Date startDate = dateFormat.parse(upcomingModel.getStart_time());
                    endDate = dateFormat.parse(upcomingModel.getEnd_time());

                    String date = dateFormatter.format(startDate);
                    if (date.endsWith("01") && !date.endsWith("11"))
                        dateFormatter = new SimpleDateFormat("d'st' MMMM");

                    else if (date.endsWith("02") && !date.endsWith("12"))
                        dateFormatter = new SimpleDateFormat("d'nd' MMMM");

                    else if (date.endsWith("03") && !date.endsWith("13"))
                        dateFormatter = new SimpleDateFormat("d'rd' MMMM");

                    else
                        dateFormatter = new SimpleDateFormat("d'th' MMMM");

                    eventDateStr = dateFormatter.format(startDate);
                    startTime = startTimeFormatter.format(startDate);
                    endTime = endTimeFormatter.format(endDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                eventVenue.setText(upcomingModel.getVenue());
                eventLocation.setText(upcomingModel.getLocation());
                eventDate.setText(eventDateStr);
                eventTime.setText(startTime + " - " + endTime);
                eventCost.setText(upcomingModel.getCost());
                Util.loadImage(eventImage, upcomingModel.getImage_url(), Util.getProgressDrawable(eventImage.getContext()));
            }

        }


    }

}
